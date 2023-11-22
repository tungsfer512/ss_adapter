
import TableBase from '@/components/Table';
import { IVanBanRecord } from '@/models/vanban';
import { IColumn } from '@/utils/interfaces';
import { CloseCircleOutlined, CheckCircleOutlined } from '@ant-design/icons';
import { Button, Divider, Popconfirm } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';
import FormVanBan from './FormVanBan';
import { ETrangThaiVanBan } from '@/utils/constants';

const VanBanDen = (): React.Fragment => {

  const vanbanModel = useModel('vanban');

  const handleTiepNhan = (record: IVanBanRecord) => {
    if (record?.pid && record?.pid !== '') {
      let headers = {
        docId: record.id,
        pDocId: record?.pid,
        status: ETrangThaiVanBan.DA_CAP_NHAT,
        to: record?.fromOrganization?.code,
      }
      vanbanModel.send_status_edoc(headers);
    } else {
      let headers = {
        docId: record.id,
        status: ETrangThaiVanBan.DA_TIEP_NHAN,
        to: record?.fromOrganization?.code,
      }
      vanbanModel.send_status_edoc(headers);
    }
  };

  const handleTuChoi = async (record: IVanBanRecord) => {
    if (record?.pid && record?.pid !== '') {
      let headers = {
        docId: record.id,
        pDocId: record?.pid,
        status: ETrangThaiVanBan.TU_CHOI_CAP_NHAT,
        to: record?.fromOrganization?.code,
      }
      vanbanModel.send_status_edoc(headers);
    } else {
      let headers = {
        docId: record.id,
        status: ETrangThaiVanBan.TU_CHOI_TIEP_NHAN,
        to: record?.fromOrganization?.code,
      }
      vanbanModel.send_status_edoc(headers);
    }
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
      title: 'STT',
      dataIndex: 'index',
      width: 80,
      align: 'center',
    },
    {
      title: 'Đơn vị gửi',
      render: (value: any, record: IVanBanRecord) => record?.fromOrganization?.code,
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'Đơn vị nhận',
    //   render: (value: any, record: IVanBanRecord) => record?.toOrganization?.code,
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
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
    // {
    //   title: 'Trạng thái VB',
    //   dataIndex: 'sendStatusDesc',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Trạng thái VB',
      dataIndex: 'receiveStatusDesc',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Mô tả',
      dataIndex: 'description',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'Số hiệu VB đi',
    //   dataIndex: 'senderDocId',
    //   search: 'search',
    //   notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Số hiệu VB đến',
      dataIndex: 'receiverDocId',
      search: 'search',
      notRegex: true,
      width: 200,
      align: 'center',
    },
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
        title="Danh sách văn bản đến"
        columns={columns}
        hascreate={false}
        border={true}
        formType={'Modal'}
        dependencies={[vanbanModel.page, vanbanModel.limit, vanbanModel.condition]}
        widthDrawer={800}
        getData={() => vanbanModel.get_received_edoc_list({ messagetype: "demo", servicetype: "demo" })}
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

export default VanBanDen;
