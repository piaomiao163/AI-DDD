declare module '@bpmn-io/properties-panel' {
  export function TextFieldEntry(props: any): any
  export function TextAreaEntry(props: any): any
  export function CheckboxEntry(props: any): any
  export function SelectEntry(props: any): any
  export function Group(props: any): any
  export function ListGroup(props: any): any
  export function ListEntry(props: any): any
  export function isTextFieldEntryEdited(entry: any): boolean
  export function isTextAreaEntryEdited(entry: any): boolean
  export function isCheckboxEntryEdited(entry: any): boolean
  export function isSelectEntryEdited(entry: any): boolean
}

declare module '@bpmn-io/properties-panel/dist/assets/properties-panel.css';
