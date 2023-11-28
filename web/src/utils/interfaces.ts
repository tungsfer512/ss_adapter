import type { ColumnType as ICol } from 'rc-table/lib/interface';

export interface IColumn<T> extends ICol<T> {
  title?: string;
  dataIndex?: string;
  width?: number;
  align?: string;
  render?: object;
  fixed?: string;
  search?: 'search' | 'filter' | 'sort' | 'filterTF' | 'filterString';
  columnKey?: string;
  notRegex?: boolean;
  typeFilter?: 'query' | 'condition';
  hide?: boolean;
}

export interface IOrganizationRecord {
  id: string;
  organId: string;
  organizationInCharge: string;
  organName: string;
  organAdd: string;
  email: string;
  telephone: string;
  fax: string;
  website: string;
}

export interface IDocumentTypeRecord {
  id: number;
  type: number;
  typeDetail: string;
  typeName: string;
}

export interface IAttachmentRecord {
  attachment_ContentType: string;
  attachment_ContentId: string;
  attachment_Description: string;
  attachment_ContentTransferEncoded: string;
  attachment_AttachmentName: string;
}

export interface IVanBanRecord {
  id: string;
  from: IOrganizationRecord;
  to: IOrganizationRecord;
  code_CodeNumber: string;
  code_CodeNotation: string;
  promulgationInfo_Place: string;
  promulgationInfo_PromulgationDate: string;
  documentType: IDocumentTypeRecord;
  subject: string;
  content: string;
  signerInfo_Competence: string;
  signerInfo_Position: string;
  signerInfo_FullName: string;
  dueDate: string;
  toPlaces: Array<string>;
  otherInfo_Priority: number;
  otherInfo_SphereOfPromulgation: string;
  otherInfo_TyperNotation: string;
  otherInfo_PromulgationAmount: number;
  otherInfo_PageAmount: number;
  appendixes: Array<string>;
  responseFor_OrganId: string;
  responseFor_Code: string;
  responseFor_PromulgationDate: string;
  responseFor_DocumentId: string;
  steeringType: number;
  documentId: string;
  statusCode: string;
  description: string;
  timestamp: string;
  traceHeaders: any;
  business_BussinessDocType: number;
  business_BussinessDocReason: string;
  business_BussinessDocumentInfo_DocumentInfo: number;
  business_BussinessDocumentInfo_DocumentReceiver: number;
  business_BussinessDocumentInfo_ReceiverList: any;
  business_DocumentId: string;
  business_StaffInfo_Department: string;
  business_StaffInfo_Staff: string;
  business_StaffInfo_Mobile: string;
  business_StaffInfo_Email: string;
  business_Paper: number;
  business_ReplacementInfoList: any;
  attachments: Array<IAttachmentRecord>;
  serviceType: string;
  messageType: string;
  senderDocId: string;
  receiverDocId: string;
  status: string;
  statusDesc: string;
  sendStatus: string;
  receiveStatus: string;
  sendStatusDesc: string;
  receiveStatusDesc: string;
  data: string;
}