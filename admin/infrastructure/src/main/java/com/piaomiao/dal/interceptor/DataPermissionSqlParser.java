package com.piaomiao.dal.interceptor;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.core.parser.SqlInfo;
import com.piaomiao.web.interceptor.DataPermissionContext;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.*;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.reflection.MetaObject;

import java.util.List;

/**
 * 数据权限 SQL 解析器
 * 用于拦截 SQL 并添加数据权限条件
 */
public class DataPermissionSqlParser implements ISqlParser {

    @Override
    public SqlInfo parser(MetaObject metaObject, String sql) {
        try {
            // 解析 SQL
            Statement statement = CCJSqlParserUtil.parse(sql);
            
            // 只处理 SELECT 语句
            if (statement instanceof Select) {
                Select select = (Select) statement;
                SelectBody selectBody = select.getSelectBody();

                // 处理简单查询
                if (selectBody instanceof PlainSelect) {
                    handlePlainSelect((PlainSelect) selectBody);
                }
                // 处理 UNION 查询
                else if (selectBody instanceof SetOperationList) {
                    SetOperationList setOperationList = (SetOperationList) selectBody;
                    List<SelectBody> selectBodies = setOperationList.getSelects();
                    for (SelectBody body : selectBodies) {
                        if (body instanceof PlainSelect) {
                            handlePlainSelect((PlainSelect) body);
                        }
                    }
                }

                // 返回修改后的 SQL
                return SqlInfo.newInstance().setSql(select.toString());
            }
            
            // 非 SELECT 语句，直接返回 null
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理简单查询
     */
    private void handlePlainSelect(PlainSelect plainSelect) {
        // 获取 FROM 子句
        FromItem fromItem = plainSelect.getFromItem();

        // 检查是否是用户表查询
        if (isUserTableQuery(fromItem)) {
            // 获取 WHERE 条件
            Expression where = plainSelect.getWhere();

            // 构建数据权限条件
            Expression dataPermissionExpression = buildDataPermissionExpression();
            if (dataPermissionExpression != null) {
                if (where == null) {
                    // 如果没有 WHERE 条件，直接设置数据权限条件
                    plainSelect.setWhere(dataPermissionExpression);
                } else {
                    // 如果有 WHERE 条件，将数据权限条件与原有条件 AND 起来
                    plainSelect.setWhere(new AndExpression(where, dataPermissionExpression));
                }
            }
        }
    }

    /**
     * 检查是否是用户表查询
     */
    private boolean isUserTableQuery(FromItem fromItem) {
        // 检查简单表
        if (fromItem instanceof Table) {
            Table table = (Table) fromItem;
            String tableName = table.getName().toLowerCase();
            return tableName.equals("sys_user") || tableName.equals("user");
        }
        // 检查 JOIN 查询
//        else if (fromItem instanceof Join) {
//            Join join = (Join) fromItem;
//            return isUserTableQuery(join.getLeftItem()) || isUserTableQuery(join.getRightItem());
//        }
        return false;
    }

    /**
     * 构建数据权限条件
     */
    private Expression buildDataPermissionExpression() {
        // 检查是否有全部数据权限
        if (DataPermissionContext.isAllData()) {
            // 全部数据权限，不需要添加条件
            return null;
        }

        // 检查是否有本人数据权限
        if (DataPermissionContext.isSelfData()) {
            // 只查询当前用户的数据
            Long currentUserId = DataPermissionContext.getCurrentUserId();
            if (currentUserId != null) {
                EqualsTo equalsTo = new EqualsTo();
                equalsTo.setLeftExpression(new Column("id"));
                equalsTo.setRightExpression(new LongValue(currentUserId));
                return equalsTo;
            }
            return null;
        }

        // 检查是否有部门数据权限
        List<Long> departmentIds = DataPermissionContext.getCurrentUserDepartmentIds();
        if (departmentIds != null && !departmentIds.isEmpty()) {
            // 只查询指定部门的数据
            InExpression inExpression = new InExpression();
            inExpression.setLeftExpression(new Column("department_id"));

            // 构建部门 ID 列表
            ExpressionList expressionList = new ExpressionList();
            for (Long departmentId : departmentIds) {
                expressionList.addExpressions(new LongValue(departmentId));
            }
            inExpression.setRightItemsList(expressionList);

            return inExpression;
        }

        // 检查是否有用户数据权限
        List<Long> userIds = DataPermissionContext.getCurrentUserIds();
        if (userIds != null && !userIds.isEmpty()) {
            // 只查询指定用户的数据
            InExpression inExpression = new InExpression();
            inExpression.setLeftExpression(new Column("id"));

            // 构建用户 ID 列表
            ExpressionList expressionList = new ExpressionList();
            for (Long userId : userIds) {
                expressionList.addExpressions(new LongValue(userId));
            }
            inExpression.setRightItemsList(expressionList);

            return inExpression;
        }

        return null;
    }
}