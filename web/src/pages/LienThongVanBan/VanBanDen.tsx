
import TableBase from '@/components/Table';
import { IVanBanRecord } from '@/models/vanban';
import { IColumn } from '@/utils/interfaces';
import { CloseCircleOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { Button, Divider, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';
import FormVanBan from './FormVanBan';

const VanBanDen = (): React.Fragment => {

  const vanbanModel = useModel('vanban');

  const handleTiepNhan = (record: IVanBanRecord) => {
    vanbanModel.setVisibleForm(true);
    vanbanModel.setEdit(true);
    vanbanModel.setRecord(record);
  };

  const handleTuChoi = async (record: IVanBanRecord) => {
    await vanbanModel.del(record?.id ?? '');
  };

  const renderLast = (value: any, record: IVanBanRecord) => (
    <React.Fragment>
      <Button
        type="primary"
        shape="circle"
        icon={<CheckCircleOutlined />}
        title="Tiếp nhận"
        onClick={() => handleTiepNhan(record)}
      />
      <Divider type="vertical" />
      <Popconfirm
        title="Bạn có chắc muốn từ chối văn bản này không?"
        okText="Có"
        cancelText="Không"
        onConfirm={() => handleTuChoi(record)}
      >
        <Button type="danger" shape="circle" icon={<CloseCircleOutlined />} title="Từ chối" />
      </Popconfirm>
    </React.Fragment>
  );
  const columns: IColumn<IVanBanRecord>[] = [
    {
      title: 'ID',
      dataIndex: 'id',
      width: 80,
      align: 'center',
    },
    {
      title: 'SenderDocId',
      dataIndex: 'senderDocId',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'ReceiverDocId',
      dataIndex: 'receiverDocId',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Pid',
      dataIndex: 'pid',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'ServiceType',
      dataIndex: 'serviceType',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'MessageType',
      dataIndex: 'messageType',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Created_time',
      dataIndex: 'created_time',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Updated_time',
      dataIndex: 'updated_time',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'From',
      dataIndex: 'from',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'To',
      dataIndex: 'to',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'SendStatus',
      dataIndex: 'sendStatus',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'ReceiveStatus',
      dataIndex: 'receiveStatus',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Title',
      dataIndex: 'title',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Notation',
      dataIndex: 'notation',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'SendStatusDesc',
      dataIndex: 'sendStatusDesc',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'ReceiveStatusDesc',
      dataIndex: 'receiveStatusDesc',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
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
        title="Danh sách văn bản đến"
        columns={columns}
        hascreate={false}
        formType={'Modal'}
        dependencies={[vanbanModel.page, vanbanModel.limit, vanbanModel.condition]}
        widthDrawer={800}
        getData={() => vanbanModel.get_received_edoc_list({messagetype: "demo", servicetype: "demo"})}
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
          },
        }}
      />
    </>
  )
};

export default VanBanDen;