export interface Course {
    id: string;
    name: string;
    topics: Array<Topic>;
}

export interface Topic {
    id: string;
    name: string;
    description: string;
}
