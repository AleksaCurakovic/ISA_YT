export interface VideoUpload {
    id: number;
    title: string;
    description: string;
    author: string;
    tags: string[];
    thumbnailUrl: string;
    videoUrl: string;
    createdAt: Date;
    geoLocation: string;
}