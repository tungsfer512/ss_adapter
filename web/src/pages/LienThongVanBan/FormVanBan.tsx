/* eslint-disable no-underscore-dangle */
import { IUserRecord } from '@/models/users';
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
  const usersModel = useModel('users');
  const groupsModel = useModel('groups');
  useEffect(() => {
    groupsModel.getData();
  }, []);
  const handleFinish = async (values: IUserRecord) => {
    values.last_login = moment().toISOString();
    if (usersModel.edit) {
      if (usersModel?.record?.id) {
        values.date_joined = moment(values.date_joined).toISOString();
        await usersModel.upd({
          ...usersModel?.record,
          ...values,
          id: usersModel?.record?.id,
        });
      } else {
        values.date_joined = moment().toISOString();
        values.is_active = true;
        values.is_staff = true;
        values.is_superuser = false;
        delete values.confirmPassword;

        await usersModel.add(values);
      }
    } else {
      values.date_joined = moment().toISOString();
      values.is_active = true;
      values.is_staff = true;
      values.is_superuser = false;
      delete values.confirmPassword;
      await usersModel.add(values);
    }
  };
  // console.log(groupsModel, 'form.getFieldValue()');
  return (
    <Spin spinning={usersModel.loading}>
      <Card title={usersModel.edit ? 'Chỉnh sửa' : 'Thêm mới'}>
        <Form
          {...layout}
          form={form}
          onFinish={handleFinish}
          initialValues={{
            ...(usersModel?.record ?? {}),
          }}
        >
          <Form.Item label="Username" name="username" rules={[...rules.required]}>
            <Input placeholder="Username" disabled={usersModel?.edit} />
          </Form.Item>
          {!usersModel?.edit && (
            <>
              <Form.Item label="Mật khẩu" name="password" rules={[...rules.required]}>
                <Input placeholder="Mật khẩu" type="password" />
              </Form.Item>
              <Form.Item
                label="Nhập lại mật khẩu"
                name="confirmPassword"
                rules={[
                  ...rules.required,
                  {
                    validator: (_, value, callback) => {
                      if (value !== form.getFieldValue('password')) callback('');
                      callback();
                    },
                    message: 'Mật khẩu không khớp',
                  },
                ]}
              >
                <Input placeholder="Nhập lại mật khẩu" type="password" />
              </Form.Item>
            </>
          )}
          <Form.Item label="Họ tên đệm" name="first_name" rules={[...rules.required]}>
            <Input placeholder="Họ tên đệm" />
          </Form.Item>
          <Form.Item label="Tên" name="last_name" rules={[...rules.required]}>
            <Input placeholder="Tên" />
          </Form.Item>
          <Form.Item label="Email" name="email" rules={[...rules.required]}>
            <Input placeholder="Email" />
          </Form.Item>
          <Form.Item label="Thuộc nhóm" name="groups" rules={[...rules.required]}>
            <Select placeholder="Thuộc nhóm..." mode="multiple">
              {groupsModel?.danhSach?.map((item) => (
                <Select.Option value={item.id}>{item.name}</Select.Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item {...tailLayout}>
            <Button type="primary" htmlType="submit">
              {usersModel.edit ? 'Cập nhật' : 'Thêm mới'}
            </Button>
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default FormVanBan;
