import { createApp } from 'vue'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import App from './App.vue'
import router from './router'

// 引入element-plus
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import locale from 'element-plus/es/locale/lang/zh-cn'; // 引入中文语言包

// 引入全局样式
import '@/assets/global.css'
import '@/assets/style.css'

// 引入全局iconfont
import '@/assets/icon/iconfont.js'
import '@/assets/icon/iconfont.css'

// 引入element-plus的icon图标
import * as ElIconModules from '@element-plus/icons'

const app = createApp(App)
const pinia = createPinia()

// 使用持久化插件
pinia.use(piniaPluginPersistedstate)

// 先使用 pinia，再使用其他插件
app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale })

// 注册所有图标
for (const iconName in ElIconModules) {
  app.component(iconName, ElIconModules[iconName])
}

app.mount('#app')
