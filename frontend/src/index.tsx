import React, { Suspense } from 'react';
import ReactDOM from 'react-dom';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Courses from './Courses';
import CourseElement from './CourseElement';
import reportWebVitals from './reportWebVitals';

import "./i18n";
import "./index.css";
import Registration from './Registration';
import Login from './Login';
import AuthProvider from './auth/AuthProvider';

ReactDOM.render(
    <React.StrictMode>
        <Suspense fallback="Loading...">
            <BrowserRouter>
                <AuthProvider>
                    <Routes>
                        <Route path="/" element={<Login />} />
                        <Route path="/registration" element={<Registration />} />
                        <Route path="/login" element={<Login />} />
                        <Route path="/courses" element={<Courses />} />
                        <Route path="/courses/:courseId" element={<CourseElement />} />
                    </Routes>
                </AuthProvider>
            </BrowserRouter>
        </Suspense>
    </React.StrictMode>,
  document.getElementById('root')
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
