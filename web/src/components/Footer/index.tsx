import { DefaultFooter } from '@ant-design/pro-layout';
import { useIntl } from 'umi';

export default () => {
  const intl = useIntl();
  const defaultMessage = intl.formatMessage({
    id: 'app.copyright.produced',
    defaultMessage: 'CopyRight',
  });

  return (
    <DefaultFooter
      style={{ backgroundColor: '#fff' }}
      copyright={`2021 ${defaultMessage}`}
      links={[
        {
          key: 'github',
          title: 'Viện Khoa học Kỹ thuật Bưu điện',
          href: 'https://ript.vn/',
          blankTarget: true,
        },
      ]}
    />
  );
};
