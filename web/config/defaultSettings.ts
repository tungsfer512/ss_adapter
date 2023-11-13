import { Settings as LayoutSettings } from '@ant-design/pro-layout';

const Settings: LayoutSettings & {
  pwa?: boolean;
  logo?: string;
} = {
  navTheme: 'dark',
  primaryColor: '#0033CD',
  layout: 'top',
  contentWidth: 'Fluid',
  fixedHeader: true,
  fixSiderbar: true,
  colorWeak: false,
  headerTheme: 'dark',
  title: 'HỆ THỐNG LIÊN THÔNG DỮ LIỆU VÀ VĂN BẢN',
  pwa: false,
  logo: '/favicon.ico',
  iconfontUrl: '',
};

export default Settings;
