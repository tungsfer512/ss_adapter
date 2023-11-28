
import TableBase from '@/components/Table';
import { IVanBanRecord } from '@/utils/interfaces';
import { IColumn } from '@/utils/interfaces';
import { DeleteOutlined, EditOutlined, RollbackOutlined } from '@ant-design/icons';
import { Button, Divider, Popconfirm, Tag } from 'antd';
import React, { useEffect, useState } from 'react';
import { useModel, history } from 'umi';
import FormVanBan from './FormVanBan';
import { ETrangThaiVanBan } from '@/utils/constants';

const VanBanDi = (): React.Fragment => {

  const vanbanModel = useModel('vanban');
  const type = "sent";

  const checkCanChinhSua = (record: IVanBanRecord) => {
    // if record.receiveStatus === ETrangThaiVanBan.
    return true;
  }

  const handleChinhSua = (record: IVanBanRecord) => {
    vanbanModel.setVisibleForm(true);
    vanbanModel.setEdit(true);
    vanbanModel.setRecord(record);
  };

  const handleThuHoi = async (record: IVanBanRecord) => {
    let headers = {
      docId: record.documentId,
    }
    let payload = {
      status_staff_info_department: "Phong hanh chinh",
      status_staff_info_staff: "Nguyen Thi Ngoc Tram",
      status_staff_info_mobile: "84912000002",
      status_staff_info_email: "ngoctram@nghean.vn",
      status_status_code: ETrangThaiVanBan.DA_YEU_CAU_THU_HOI,
      status_description: "Đã từ chối tiếp nhận"
    }
    vanbanModel.send_status_edoc(headers, payload, type);
  };

  const renderLast = (value: any, record: IVanBanRecord) => (
    <div style={{ display: 'flex', width: '100%', flexDirection: 'column', alignItem: 'center' }}>
      <Button
        type="primary"
        style={{ marginBottom: '3px', flex: 1 }}
        title="Cập nhật"
        onClick={() => handleChinhSua(record)}
      >Cập nhật</Button>
      <Popconfirm
        title="Bạn có chắc rằng muốn thu hồi văn bản này?"
        okText="Có"
        cancelText="Không"
        onConfirm={() => handleThuHoi(record)}
      >
        <Button
          type="danger"
          style={{ marginTop: '3px', flex: 1 }}
          title="Thu hồi" >Thu hồi</Button>
      </Popconfirm>
    </div>
  );

  const renderDoKhan = (value: any, record: IVanBanRecord) => (
    <div style={{ display: 'flex', width: '100%', flexDirection: 'column', alignItem: 'center' }}>
      {record.otherInfo_Priority == 0 && <Tag color="blue">Thường</Tag>}
      {record.otherInfo_Priority == 1 && <Tag color="orange">Khẩn</Tag>}
      {record.otherInfo_Priority == 2 && <Tag color="magenta">Thượng khẩn</Tag>}
      {record.otherInfo_Priority == 3 && <Tag color="red">Hỏa tốc</Tag>}
      {record.otherInfo_Priority == 4 && <Tag color="purple">Hỏa tốc hẹn giờ</Tag>}
    </div>
  );

  const columns: IColumn<IVanBanRecord>[] = [
    {
      title: 'STT',
      dataIndex: 'index',
      width: 80,
      align: 'center',
    },
    // {
    //   title: 'ID',
    //   dataIndex: 'id',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'Đơn vị gửi',
    //   dataIndex: 'from',
    //   search: 'search',
    //   // notRegex: true,
    //   render: (value: any, record: IVanBanRecord) => record.from.organName,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Đơn vị nhận',
      dataIndex: 'to',
      search: 'search',
      // notRegex: true,
      render: (value: any, record: IVanBanRecord) => record.to.organName,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'code_CodeNumber',
    //   dataIndex: 'code_CodeNumber',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'code_CodeNotation',
    //   dataIndex: 'code_CodeNotation',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'promulgationInfo_Place',
    //   dataIndex: 'promulgationInfo_Place',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Tiêu đề',
      dataIndex: 'subject',
      search: 'search',
      // notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Trích yếu nội dung VB',
      dataIndex: 'content',
      search: 'search',
      // notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'signerInfo_Competence',
    //   dataIndex: 'signerInfo_Competence',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'signerInfo_Position',
    //   dataIndex: 'signerInfo_Position',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'signerInfo_FullName',
    //   dataIndex: 'signerInfo_FullName',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Hạn xử lý',
      dataIndex: 'dueDate',
      search: 'search',
      // notRegex: true,
      width: 120,
      align: 'center',
    },
    // {
    //   title: 'toPlaces',
    //   dataIndex: 'toPlaces',
    //   render: (value: any, record: IVanBanRecord) => 
    //   {
    //     let toPlaces = '';
    //     record.toPlaces.forEach((toPlace) => {
    //       toPlaces += toPlace + '\n';
    //     })
    //     return toPlaces;
    //   },
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'otherInfo_SphereOfPromulgation',
    //   dataIndex: 'otherInfo_SphereOfPromulgation',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'otherInfo_TyperNotation',
    //   dataIndex: 'otherInfo_TyperNotation',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'otherInfo_PromulgationAmount',
    //   dataIndex: 'otherInfo_PromulgationAmount',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'otherInfo_PageAmount',
    //   dataIndex: 'otherInfo_PageAmount',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'Phụ lục',
    //   dataIndex: 'appendixes',
    //   search: 'search',
    //   // notRegex: true,
    //   render: (value: any, record: IVanBanRecord) => 
    //   {
    //     let appendixes = '';
    //     record.appendixes.forEach((appendix) => {
    //       appendixes += appendix + '\n';
    //     })
    //     return appendixes;
    //   },
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'responseFor_OrganId',
    //   dataIndex: 'responseFor_OrganId',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'responseFor_Code',
    //   dataIndex: 'responseFor_Code',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'responseFor_PromulgationDate',
    //   dataIndex: 'responseFor_PromulgationDate',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    {
      title: 'Ngày phát hành VB',
      dataIndex: 'promulgationInfo_PromulgationDate',
      search: 'search',
      // notRegex: true,
      width: 120,
      align: 'center',
    },
    {
      title: 'Loại VB',
      dataIndex: 'documentType',
      search: 'search',
      // notRegex: true,
      render: (value: any, record: IVanBanRecord) => record.documentType.typeName,
      width: 100,
      align: 'center',
    },
    {
      title: 'Mã văn bản',
      dataIndex: 'documentId',
      search: 'search',
      // notRegex: true,
      width: 200,
      align: 'center',
    },
    {
      title: 'Mã VB được phản hồi',
      dataIndex: 'responseFor_DocumentId',
      search: 'search',
      // notRegex: true,
      width: 200,
      align: 'center',
    },
    // {
    //   title: 'steeringType',
    //   dataIndex: 'steeringType',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'statusCode',
    //   dataIndex: 'statusCode',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'description',
    //   dataIndex: 'description',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'timestamp',
    //   dataIndex: 'timestamp',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'traceHeaders',
    //   dataIndex: 'traceHeaders',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_BussinessDocType',
    //   dataIndex: 'business_BussinessDocType',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_BussinessDocReason',
    //   dataIndex: 'business_BussinessDocReason',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_BussinessDocumentInfo_DocumentInfo',
    //   dataIndex: 'business_BussinessDocumentInfo_DocumentInfo',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_BussinessDocumentInfo_DocumentReceiver',
    //   dataIndex: 'business_BussinessDocumentInfo_DocumentReceiver',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_BussinessDocumentInfo_ReceiverList',
    //   dataIndex: 'business_BussinessDocumentInfo_ReceiverList',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_DocumentId',
    //   dataIndex: 'business_DocumentId',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_StaffInfo_Department',
    //   dataIndex: 'business_StaffInfo_Department',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_StaffInfo_Staff',
    //   dataIndex: 'business_StaffInfo_Staff',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_StaffInfo_Mobile',
    //   dataIndex: 'business_StaffInfo_Mobile',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_StaffInfo_Email',
    //   dataIndex: 'business_StaffInfo_Email',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_Paper',
    //   dataIndex: 'business_Paper',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'business_ReplacementInfoList',
    //   dataIndex: 'business_ReplacementInfoList',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'File đính kèm',
    //   dataIndex: 'attachments',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'serviceType',
    //   dataIndex: 'serviceType',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'messageType',
    //   dataIndex: 'messageType',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'senderDocId',
    //   dataIndex: 'senderDocId',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'receiverDocId',
    //   dataIndex: 'receiverDocId',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'status',
    //   dataIndex: 'status',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'statusDesc',
    //   dataIndex: 'statusDesc',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'sendStatus',
    //   dataIndex: 'sendStatus',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'receiveStatus',
    //   dataIndex: 'receiveStatus',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'data',
    //   dataIndex: 'data',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 200,
    //   align: 'center',
    // },
    // {
    //   title: 'Trạng thái VB',
    //   dataIndex: 'receiveStatusDesc',
    //   search: 'search',
    //   // notRegex: true,
    //   width: 150,
    //   fixed: 'right',
    //   align: 'center',
    // },
    {
      title: 'Trạng thái VB',
      dataIndex: 'sendStatusDesc',
      search: 'search',
      // notRegex: true,
      width: 150,
      fixed: 'right',
      align: 'center',
    },
    {
      title: 'Độ khẩn',
      dataIndex: 'otherInfo_Priority',
      search: 'search',
      // notRegex: true,
      render: (value: any, record: IVanBanRecord) => renderDoKhan(value, record),
      fixed: 'right',
      width: 150,
      align: 'center',
    },
    {
      title: 'Thao tác',
      align: 'center',
      render: (value: any, record: IVanBanRecord) => renderLast(value, record),
      fixed: 'right',
      width: 130,
    },
  ];

  return (
    <>
      <TableBase
        loading={vanbanModel.loading}
        modelName={'vanban'}
        title="Danh sách văn bản đi - Phòng Văn Thư"
        columns={columns}
        hascreate={true}
        border={true}
        formType={'Modal'}
        dependencies={[vanbanModel.page, vanbanModel.limit, vanbanModel.condition]}
        widthDrawer={800}
        getData={() => vanbanModel.get_sent_edoc_list({ messagetype: "demo", servicetype: "demo" })}
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
