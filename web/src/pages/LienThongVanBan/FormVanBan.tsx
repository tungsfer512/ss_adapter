/* eslint-disable no-underscore-dangle */
import { IOrganizationRecord, IVanBanRecord } from '@/utils/interfaces';
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
  const donviModel = useModel('donvi');
  const [selectedFile, setSelectedFile] = useState(null);

  const handleFileSelect = (event: any) => {
    setSelectedFile(event.target.files)
  }

  const handleFinish = (values: any) => {
    let payload = new FormData();
    if (vanbanModel.edit) {
      // payload.append('files', selectedFile[0]);
      // payload.append('files', selectedFile[1]);
      // let headers = {
      //   to: vanbanModel?.record?.to?.organId,
      //   pDocId: vanbanModel?.record?.id,
      // }
      // vanbanModel.send_edoc(headers, payload);
      window.alert("Nghiệp vụ cập nhật chưa có!")
    } else {
      for (let file of selectedFile) {
        payload.append('files', file);
      }
      let json_data = {
        'fromId': 'CS:GOV:SS1MC:SS1SUB1',
        'toIds': values.to_ids,
        'codeNumber': values.code_number,
        'codeNotation': values.code_notation,
        'promulgationPlace': values.promulgation_place,
        'documentTypeId': Number(values.document_type_id),
        'subject': values.subject,
        'content': values.content,
        'signerInfoCompetence': values.signer_info_competence,
        'signerInfoPosition': values.signer_info_position,
        'signerInfoFullname': values.signer_info_fullname,
        'dueDate': values.due_date.replaceAll('-', '/'),
        'toPlaces': ['to_places_01', 'to_places_02', 'to_places_03'],
        'otherInfoPriority': Number(values.other_info_priority),
        'otherInfoSphereOfPromulgation': values.other_info_sphere_of_promulgation,
        'otherInfoTyperNotation': values.other_info_typer_notation,
        'otherInfoPromulgationAmount': Number(values.other_info_promulgation_amount),
        'otherInfoPageAmount': Number(values.other_info_page_amount),
        'appendixes': ['appendixes_01', 'appendixes_02', 'appendixes_03'],
        'responseForDocumentId': undefined,
        'steeringType': Number(values.steering_type),
        'businessBussinessDocType': 0,
        'businessBussinessDocReason': 'VB mới',
        'businessBussinessDocumentInfoDocumentInfo': Number(values.business_bussiness_document_info_document_info),
        'businessBussinessDocumentInfoDocumentReceiver': Number(values.business_bussiness_document_info_document_receiver),
        'businessBussinessDocumentInfoReceiverJsonStrList': [
          JSON.stringify({
            'updateReceiverReceiverType': 1,
            'updateReceiverOrganId': 'CS:GOV:SS1MC:SS1SUB1',
          }),
          JSON.stringify({
            'updateReceiverReceiverType': 0,
            'updateReceiverOrganId': 'CS:GOV:SS2MC:SS2SUB1',
          }),
        ],
        'businessDocumentId': values.business_document_id,
        'businessStaffInfoDepartment': values.business_staff_info_department,
        'businessStaffInfoStaff': values.business_staff_info_staff,
        'businessStaffInfoMobile': values.business_staff_info_mobile,
        'businessStaffInfoEmail': values.business_staff_info_email,
        'businessPaper': Number(values.business_paper),
        'businessReplacementInfoJsonStrList': [
          JSON.stringify({
            'replacementInfoDocumentId': 'replacement_info_document_id_01',
            'replacementInfoOrganIdList': [
              'CS:GOV:SS1MC:SS1SUB1',
              'CS:GOV:SS2MC:SS2SUB1',
            ],
          }),
          JSON.stringify({
            'replacementInfoDocumentId': 'replacement_info_document_id_02',
            'replacementInfoOrganIdList': [
              'CS:GOV:SS1MC:SS1SUB1',
              'CS:GOV:SS2MC:SS2SUB1',
            ],
          }),
        ],
        'attachmentDescriptionList': [
          'attachment_description_list_01',
          'attachment_description_list_02',
          'attachment_description_list_03',
          'attachment_description_list_04',
          'attachment_description_list_05',
          'attachment_description_list_06',
        ],
      }
      console.log(json_data);

      payload.append('json_data', JSON.stringify(json_data));
      let headers = {}
      vanbanModel.send_edoc(headers, payload);
    }
    vanbanModel.setVisibleForm(false);
  };

  useEffect(() => {
    donviModel.get_all_don_vi();
  }, [])

  return (
    <Spin spinning={vanbanModel.loading}>
      <Card title={vanbanModel?.edit ? 'Cập nhật văn bản' : 'Gửi văn bản mới'}>
        <Form
          {...layout}
          form={form}
          onFinish={handleFinish}
          initialValues={{
            ...(vanbanModel?.record ?? {}),
          }}
        >
          <Form.Item name={['to_ids']} label='Mã đơn vị nhận' rules={rules.required}>
            {vanbanModel?.edit ?
              <Input type='text' name={['to_ids']} readOnly disabled />
              :
              <Select placeholder="Mã đơn vị nhận" mode="multiple">
                {
                  donviModel.danhSach.map((donvi: IOrganizationRecord) => (
                    <Select.Option value={donvi.organId}>{donvi.organName}</Select.Option>
                  ))
                }
              </Select>
            }
          </Form.Item>
          <Form.Item name={['code_number']} label='Số hiệu văn bản' rules={rules.required}>
            <Input type='text' name={['code_number']} />
          </Form.Item>
          <Form.Item name={['code_notation']} label='Ký hiệu văn bản' rules={rules.required}>
            <Input type='text' name={['code_notation']} />
          </Form.Item>
          <Form.Item name={['promulgation_place']} label='Địa điểm phát hành' rules={rules.required}>
            <Input type='text' name={['promulgation_place']} />
          </Form.Item>
          <Form.Item name={['document_type_id']} label='Loại văn bản' rules={rules.required}>
            <Select placeholder="Loại văn bản">
              <Select.Option value={1}>Công văn</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name={['subject']} label='Tiêu đề văn bản' rules={rules.required}>
            <Input type='text' name={['subject']} />
          </Form.Item>
          <Form.Item name={['content']} label='Trích yếu nội dung VB' rules={rules.required}>
            <Input type='text' name={['content']} />
          </Form.Item>
          <Form.Item name={['signer_info_competence']} label='Quyền hạn người ký' rules={rules.required}>
            <Input type='text' name={['signer_info_competence']} />
          </Form.Item>
          <Form.Item name={['signer_info_position']} label='Chức vụ người ký' rules={rules.required}>
            <Input type='text' name={['signer_info_position']} />
          </Form.Item>
          <Form.Item name={['signer_info_fullname']} label='Họ tên người ký' rules={rules.required}>
            <Input type='text' name={['signer_info_fullname']} />
          </Form.Item>
          <Form.Item name={['due_date']} label='Hạn xử lý' rules={rules.required}>
            <Input type='date' name={['due_date']} />
          </Form.Item>
          <Form.Item name={['other_info_priority']} label='Độ ưu tiên văn bản' rules={rules.required}>
            <Select placeholder="Loại văn bản">
              <Select.Option value={0}>Thường</Select.Option>
              <Select.Option value={1}>Khẩn</Select.Option>
              <Select.Option value={2}>Thượng khẩn</Select.Option>
              <Select.Option value={3}>Hỏa tốc</Select.Option>
              <Select.Option value={4}>Hỏa tốc hẹn giờ</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name={['other_info_sphere_of_promulgation']} label='Phạm vi lưu hành VB' rules={rules.required}>
            <Input type='text' name={['other_info_sphere_of_promulgation']} />
          </Form.Item>
          <Form.Item name={['other_info_typer_notation']} label='Tên người xử lý(viết tắt)' rules={rules.required}>
            <Input type='text' name={['other_info_typer_notation']} />
          </Form.Item>
          <Form.Item name={['other_info_promulgation_amount']} label='Số lượng phát hành' rules={rules.required}>
            <Input type='number' name={['other_info_promulgation_amount']} />
          </Form.Item>
          <Form.Item name={['other_info_page_amount']} label='Số trang' rules={rules.required}>
            <Input type='number' name={['other_info_page_amount']} />
          </Form.Item>
          <Form.Item name={['response_for']} label='Phản hồi' rules={rules.required}>
            <Select placeholder="Phản hồi cho văn bản khác?">
              <Select.Option value={true}>Có</Select.Option>
              <Select.Option value={false}>Không</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name={['steering_type']} label='Loại chỉ đạo' rules={rules.required}>
            <Select placeholder="Loại chỉ đạo">
              <Select.Option value={0}>Không phải chỉ đạo</Select.Option>
              <Select.Option value={1}>Chỉ đạo</Select.Option>
              <Select.Option value={2}>Báo cáo chỉ đạo</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name={['business_bussiness_document_info_document_info']} label='Cập nhật VB?' rules={rules.required}>
            <Select placeholder="Cập nhật thông tin văn bản?">
              <Select.Option value={0}>Không</Select.Option>
              <Select.Option value={1}>Có</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name={['business_bussiness_document_info_document_receiver']} label='Cập nhật đơn vị nhận?' rules={rules.required}>
            <Select placeholder="Cập nhật thông tin đơn vị nhận?">
              <Select.Option value={0}>Không</Select.Option>
              <Select.Option value={1}>Có</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name={['business_document_id']} label='Mã văn bản' rules={rules.required}>
            <Input type='text' name={['business_document_id']} />
          </Form.Item>
          <Form.Item name={['business_staff_info_department']} label='Địa điểm' rules={rules.required}>
            <Input type='text' name={['business_staff_info_department']} />
          </Form.Item>
          <Form.Item name={['business_staff_info_staff']} label='Người xử lý' rules={rules.required}>
            <Input type='text' name={['business_staff_info_staff']} />
          </Form.Item>
          <Form.Item name={['business_staff_info_mobile']} label='SĐT người xử lý' rules={rules.required}>
            <Input type='text' name={['business_staff_info_mobile']} />
          </Form.Item>
          <Form.Item name={['business_staff_info_email']} label='Email người xử lý' rules={rules.required}>
            <Input type='text' name={['business_staff_info_email']} />
          </Form.Item>
          <Form.Item name={['business_paper']} label='VB giấy?' rules={rules.required}>
            <Select placeholder="Gửi kèm VB giấy?">
              <Select.Option value={0}>Không</Select.Option>
              <Select.Option value={1}>Có</Select.Option>
            </Select>
          </Form.Item>
          <Form.Item name='files' label='File đính kèm' rules={rules.required}>
            <Input type='file' name='files' onChange={handleFileSelect} multiple />
          </Form.Item>
          <Form.Item {...tailLayout}>
            {vanbanModel?.edit ?
              <Button type='primary' htmlType='submit'>
                {'Cập nhật văn bản'}
              </Button>
              :
              <Button type='primary' htmlType='submit'>
                {'Gửi văn bản'}
              </Button>
            }
          </Form.Item>
        </Form>
      </Card>
    </Spin>
  );
};

export default FormVanBan;
