export interface Course {
    id: string;
    name: string;
    topics: Array<Topic>;
    links: Array<Link>;
}

export interface Topic {
    id: string;
    name: string;
    description: string;
    allVotes: number;
    userVotes: number;
    links: Array<Link>;
}

interface Link {
    href: string;
    rel: string;
}
