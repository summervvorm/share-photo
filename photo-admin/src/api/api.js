import api from "./http.js"
export const reqLogin = (parmas)=> api.post('/login',parmas)