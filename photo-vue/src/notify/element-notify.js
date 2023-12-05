import {
  Notification
} from 'element-ui'

export const ele_success = (message) => {
  Notification({
    message,
    type: 'success'
  });
}
export const ele_info = (message) => {
  Notification({
    message,
    type: 'info'
  });
}
export const ele_warning = (message) => {
  Notification({
    message,
    type: 'warning'
  });
}
export const ele_error = (message) => {
  Notification({
    message,
    type: 'error'
  });
}
