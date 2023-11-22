
import TableBase from '@/components/Table';
import { IVanBanRecord } from '@/models/vanban';
import { IColumn } from '@/utils/interfaces';
import { DeleteOutlined, EditOutlined, RollbackOutlined } from '@ant-design/icons';
import { Button, Divider, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';
import FormVanBan from './FormVanBan';
import { ETrangThaiVanBan } from '@/utils/constants';

const VanBanDi = (): React.Fragment => {

  const vanbanModel = useModel('vanban');

  const handleChinhSua = (record: IVanBanRecord) => {
    vanbanModel.setVisibleForm(true);
    vanbanModel.setEdit(true);
    vanbanModel.setRecord(record);
  };

  const handleThuHoi = async (record: IVanBanRecord) => {
    let headers = {
      docId: record.id,
      status: ETrangThaiVanBan.DA_THU_HOI,
      to : record?.toOrganization?.code,
    }
    vanbanModel.send_status_edoc(headers);
  };

  const renderLast = (value: any, record: IVanBanRecord) => (
    <React.Fragment>
      <Button
        type="primary"
        shape="circle"
        icon={<EditOutlined />}
        title="Chỉnh sửa"
        onClick={() => handleChinhSua(record)}
      />
      <Divider type="vertical" />
      <Popconfirm
        title="Bạn có chắc rằng muốn thu hồi văn bản này?"
        okText="Có"
        cancelText="Không"
        onConfirm={() => handleThuHoi(record)}
      >
        <Button type="danger" shape="circle" icon={<RollbackOutlined />} title="Thu hồi" />
      </Popconfirm>
    </React.Fragment>
  );
  const columns: IColumn<IVanBanRecord>[] = [
    {
      title: 'STT',
      dataIndex: 'index',
      width: 80,
      align: 'center',
    },
    // {
    //   title: 'Đơn vị gửi',
    //   render: (value: any, record: IVanBanRecord) => record?.fromOrganization?.code,
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Đơn vị nhận',
      render: (value: any, record: IVanBanRecord) => record?.toOrganization?.code,
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Tiêu đề',
      dataIndex: 'title',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Ghi chú',
      dataIndex: 'notation',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Trạng thái VB',
      dataIndex: 'sendStatusDesc',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'Trạng thái VB',
    //   dataIndex: 'receiveStatusDesc',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Số hiệu VB đi',
      dataIndex: 'senderDocId',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'Số hiệu VB đến',
    //   dataIndex: 'receiverDocId',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Mã văn bản gốc',
      dataIndex: 'pid',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'ServiceType',
    //   dataIndex: 'serviceType',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'MessageType',
    //   dataIndex: 'messageType',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Thời gian tạo',
      dataIndex: 'created_time',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Thời gian cập nhật',
      dataIndex: 'updated_time',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'Data',
    //   dataIndex: 'data',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'SendStatus',
    //   dataIndex: 'sendStatus',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'ReceiveStatus',
    //   dataIndex: 'receiveStatus',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Thao tác',
      align: 'center',
      render: (value: any, record: IVanBanRecord) => renderLast(value, record),
      fixed: 'right',
      width: 120,
    },
  ];

  return (
    <>
      <TableBase
        loading={vanbanModel.loading}
        modelName={'vanban'}
        title="Danh sách văn bản đi"
        columns={columns}
        hascreate={true}
        border={true}
        formType={'Modal'}
        dependencies={[vanbanModel.page, vanbanModel.limit, vanbanModel.condition]}
        widthDrawer={800}
        getData={() => vanbanModel.get_sent_edoc_list({messagetype: "demo", servicetype: "demo"})}
        Form={FormVanBan}
        noCleanUp={true}
        params={{
          page: vanbanModel.page,
          limit: vanbanModel.limit,
          condition: vanbanModel.condition,
        }}
        maskCloseableForm={true}
        otherProps={{
          scroll: {
            x: 1000,
            y: 535,
          },
        }}
      />
    </>
  )
};

export default VanBanDi;
