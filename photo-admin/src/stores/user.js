import { defineStore } from 'pinia'

const useUserStore = defineStore('userStore', {
  state: () => {
    return {
      currentUrl: '',
      userVisible: false,
      userInfo: '',
      token: '',
      accessArticles: [],
      tab: 0,
      page: 1
    }
  },
  actions: {},
  persist: {
    storage: window.sessionStorage
  }
})
export default useUserStore
