export interface Pagable<T> {
    content: T[];
    totalElements: number; 
    totalPages: number;
    pageNumber: number;
    pageSize: number;
}