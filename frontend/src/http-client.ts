import { NavigateFunction } from "react-router-dom"
import { Course, Topic } from "./model"

export function fetchAllCourses(token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}/api/courses`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
        return response.json();
    })
    .catch(() => navigate('/login'))
}

export function fetchCourse(courseId: string, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${courseId}`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
        return response.json();
    })
    .catch(() => navigate('/login'))
}

export function createCourse(courseName: string, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}/api/courses`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            name: courseName,
            topics: []
        })
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
    })
    .catch(() => navigate('/login'))
}

export function deleteCourse(course: Course, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}${course.links.find(l => l.rel === 'self')?.href}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401) {
            navigate('/login')
        } else if (response.status === 403) {
            throw new Error('errorActionForbidden');
        }
    })
}

export function addTopicToCourse(course: Course, topicName: string, topicDescription: string, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}${course.links.find(l => l.rel === 'create-topic')?.href}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
            name: topicName,
            description: topicDescription
        })
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
    })
    .catch(() => navigate('/login'))
}

export function fetchVotes(courseId: string, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}/api/courses/${courseId}/votes`, {
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
        return response.text();
    })
    .catch(() => navigate('/login'))
}

export function deleteTopic(topic: Topic, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}${topic.links.find(l => l.rel === 'self')?.href}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error('errorActionForbidden');
        }
    })
    .catch(() => navigate('/login'))
}

export function voteForTopic(topic: Topic, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}${topic.links.find(l => l.rel === 'vote')?.href}`, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
        return response.json()
    })
    .catch(() => navigate('/login'))
}

export function removeVoteForTopic(topic: Topic, token: string, navigate: NavigateFunction) {
    return fetch(`${process.env.REACT_APP_BASE_URL}${topic.links.find(l => l.rel === 'vote')?.href}`, {
        method: 'DELETE',
        headers: {
            'Authorization': `Bearer ${token}`
        }
    })
    .then(response => {
        if (response.status === 401 || response.status === 403) {
            throw new Error();
        }
        return response.json()
    })
    .catch(() => navigate('/login'))
}