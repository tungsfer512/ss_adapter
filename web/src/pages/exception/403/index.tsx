import { Link } from 'umi';
import { Result, Button } from 'antd';

export default () => (
  <Result
    status="403"
    title="403"
    style={{
      background: 'none',
    }}
    subTitle="Xin lỗi, bạn không có quyền truy cập trang này."
    extra={
      <Link to="/">
        <Button type="primary">Về trang chủ</Button>
      </Link>
    }
  />
);
