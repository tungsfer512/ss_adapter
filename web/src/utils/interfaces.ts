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
