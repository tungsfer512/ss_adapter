/* eslint-disable no-underscore-dangle */
import { IVanBanRecord } from '@/models/vanban';
import rules from '@/utils/rules';
import { Form, Input, Button, Card, Select, Spin, InputNumber, Col, Row } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel } from 'umi';
import moment from 'moment';

const layout = {
  labelCol: { span: 5 },
  wrapperCol: { span: 15 },
};
const tailLayout = {
  wrapperCol: { offset: 8, span: 16 },
};

const FormVanBan = () => {
  const [form] = Form.useForm();
  const vanbanModel = useModel('vanban');
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileSelect = (event: any) => {
    setSelectedFile(event.target.files)
  }

  const handleFinish = (values: any) => {
    console.log("==================");
    console.log(selectedFile[0]);
    console.log("==================");
    let payload = new FormData();
    payload.append('file', selectedFile[0]);
    let headers = {
      to : 'CS:GOV:SS2MC:SS2SUB1'
    }
    vanbanModel.send_edoc(headers, payload);
    vanbanModel.setVisibleForm(false);
  };
  return (
    <Spin spinning={vanbanModel.loading}>
      <Card title={'Thêm mới'}>
        <Form
          {...layout}
          form={form}
          onFinish={handleFinish}
          initialValues={{
            ...(vanbanModel?.record ?? {}),
          }}
        >
          <Form.Item name='file' label="file" rules={rules.required}>
            <Input type="file" name='file' onChange={handleFileSelect}/>
          </Form.Item>
          <Form.Item {...tailLayout}>
            <Button type="primary" htmlType="submit">
              {'Gửi văn bản'}
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default FormVanBan;
