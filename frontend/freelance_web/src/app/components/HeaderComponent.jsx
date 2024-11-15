import {useAuth} from "../security/AuthContext";
import {Link, useLocation, useNavigate} from "react-router-dom";
import '../style/header.css';

import logoutIcon from '../resourses/logout.svg';

import * as React from "react";

function HeaderCustomer() {
    const location = useLocation()
    const navigate = useNavigate()

    return (
        <header className="flex  max-md:flex-col max-md:gap-0">
            <div className="flex flex-col  max-md:ml-0 max-md:w-full">
                <div className="flex grow gap-5 mt-1.5 text-3xl font-extrabold text-neutral-800 max-md:mt-10"
                     onClick={() => navigate("/")}>
                    <img
                        src="https://cdn.builder.io/api/v1/image/assets/TEMP/d7e421e5471bc993b12b2d1b98b0e857e80eb65eeb579871e3ff4f8d40c146e0?apiKey=c80eac4c63644d34829eb5b5278b03df&"
                        alt="Freelance logo" className="shrink-0 self-start w-8 aspect-[1.23] mt-1.5"/>
                    <div className="flex-auto">
                        <span>Freelance</span><span className="text-blue-500">B</span>
                    </div>
                </div>
            </div>
            <div className="flex flex-col ml-auto max-md:ml-0 max-md:w-full">
                <div className="flex grow gap-4 items-center text-2xl font-semibold text-black max-md:mt-10">
                    <Link to="/customer/profile" className="no-underline text-black">
                        <img
                            src="https://cdn.builder.io/api/v1/image/assets/TEMP/c9713cfcc1760e07b8b93d354560da75fed2579f974465c888d2c0535739c115?apiKey=c80eac4c63644d34829eb5b5278b03df&"
                            alt="User avatar" className="shrink-0 self-stretch aspect-[0.95] w-[38px]"/>
                    </Link>
                    <div className="flex-auto self-stretch my-auto">{localStorage.getItem("email")}</div>

                    <Link to="/logout" className="no-underline text-black flex-auto">
                        <img
                            src={logoutIcon}
                            alt="Logout icon" className="shrink-0 self-stretch aspect-[0.95] w-[30px]"/>
                    </Link>
                </div>
            </div>
        </header>
    );
}

function HeaderContractor() {
    const location = useLocation()
    const navigate = useNavigate()

    return (
        <header className="flex gap-5 max-md:flex-col max-md:gap-0">
            <div className="flex flex-col w-[18%] max-md:ml-0 max-md:w-full">
                <div className="flex grow gap-5 mt-1.5 text-3xl font-extrabold text-neutral-800 max-md:mt-10"
                     onClick={() => navigate("/")}>
                    <img
                        src="https://cdn.builder.io/api/v1/image/assets/TEMP/d7e421e5471bc993b12b2d1b98b0e857e80eb65eeb579871e3ff4f8d40c146e0?apiKey=c80eac4c63644d34829eb5b5278b03df&"
                        alt="Freelance logo" className="shrink-0 self-start w-8 aspect-[1.23] mt-1.5"/>
                    <div className="flex-auto">
                        <span>Freelance</span><span className="text-blue-500">B</span>
                    </div>
                </div>
            </div>
            <nav className="flex flex-col ml-3 w-[30%] max-md:ml-0 max-md:w-full">
                <div
                    className="flex gap-5 mt-3.5 text-2xl font-bold text-black max-md:flex-wrap max-md:mt-10 max-md:max-w-full">
                    <div className="flex-auto">
                        <Link to="/contractor/tasks"
                              className={/^\/contractor\/tasks(?:\/|$)/.test(location.pathname) ? 'no-underline font-extrabold text-blue-600' : 'no-underline text-black'}>All
                            tasks</Link>
                    </div>
                    <div className="flex-auto">
                        <Link to="/contractor/your_tasks"
                              className={/^\/contractor\/your_tasks(?:\/|$)/.test(location.pathname) ? 'no-underline font-extrabold text-blue-600' : 'no-underline text-black'}>Your
                            tasks</Link>
                    </div>
                </div>
            </nav>
            <div className="flex flex-col ml-auto w-[26%] max-md:ml-0 max-md:w-full">
                <div className="flex grow gap-4 items-center text-2xl font-semibold text-black max-md:mt-10">
                    <Link to="/contractor/profile" className="no-underline text-black">
                        <img
                            src="https://cdn.builder.io/api/v1/image/assets/TEMP/c9713cfcc1760e07b8b93d354560da75fed2579f974465c888d2c0535739c115?apiKey=c80eac4c63644d34829eb5b5278b03df&"
                            alt="User avatar" className="shrink-0 self-stretch aspect-[0.95] w-[38px]"/>
                    </Link>
                    <div className="flex-auto self-stretch my-auto">{localStorage.getItem("email")}</div>

                    <Link to="/logout" className="no-underline text-black flex-auto">
                        <img
                            src={logoutIcon}
                            alt="Logout icon" className="shrink-0 self-stretch aspect-[0.95] w-[30px]"/>
                    </Link>
                </div>
            </div>
        </header>
    );
}

function HeaderAdmin() {
    const location = useLocation()
    const navigate = useNavigate()

    return (
        <header className="flex gap-5 max-md:flex-col max-md:gap-0">
            <div className="flex flex-col w-[18%] max-md:ml-0 max-md:w-full">
                <div className="flex grow gap-5 mt-1.5 text-3xl font-extrabold text-neutral-800 max-md:mt-10"
                     onClick={() => navigate("/")}>
                    <img
                        src="https://cdn.builder.io/api/v1/image/assets/TEMP/d7e421e5471bc993b12b2d1b98b0e857e80eb65eeb579871e3ff4f8d40c146e0?apiKey=c80eac4c63644d34829eb5b5278b03df&"
                        alt="LightClass logo" className="shrink-0 self-start w-8 aspect-[1.23] mt-1.5"/>
                    <div className="flex-auto">
                        <span>Freelance</span><span className="text-blue-500">B</span>
                    </div>
                </div>
            </div>
            <nav className="flex flex-col ml-3 w-[30%] max-md:ml-0 max-md:w-full">
                <div
                    className="flex gap-5 mt-3.5 text-2xl font-bold text-black max-md:flex-wrap max-md:mt-10 max-md:max-w-full">
                    <div className="flex-auto">
                        <Link to="/admin/fields"
                              className={/^\/admin\/fields(?:\/|$)/.test(location.pathname) ? 'no-underline font-extrabold text-blue-600' : 'no-underline text-black'}>Fields</Link>
                    </div>
                    <div className="flex-auto">
                        <Link to="/admin/statistic"
                              className={/^\/admin\/statistic(?:\/|$)/.test(location.pathname) ? 'no-underline font-extrabold text-blue-600' : 'no-underline text-black'}>Statistic</Link>
                    </div>
                </div>
            </nav>
            <div className="flex flex-col ml-auto w-[26%] max-md:ml-0 max-md:w-full">
                <div className="flex grow gap-4 items-center text-2xl font-semibold text-black max-md:mt-10">

                    <div className="flex-auto self-stretch my-auto">{localStorage.getItem("email")}</div>

                    <Link to="/logout" className="no-underline text-black flex-auto">
                        <img
                            src={logoutIcon}
                            alt="Logout icon" className="shrink-0 self-stretch aspect-[0.95] w-[30px]"/>
                    </Link>
                </div>
            </div>
        </header>
    );
}

export default function HeaderComponent() {
    const currentLocation = useLocation()
    return (
        <div
            className={currentLocation.pathname === '/' || currentLocation.pathname === '/registration' ? 'hidden' : 'd-block'}>
            <div className="w-full px-16 py-10 bg-zinc-300 max-md:px-5">
                {localStorage.getItem("role") === 'CUSTOMER' ? (<HeaderCustomer/>
                ) : (
                    localStorage.getItem("role") === 'CONTRACTOR' ? <HeaderContractor/> : (
                        localStorage.getItem("role") === 'ADMIN' && <HeaderAdmin/>
                    )
                )}
            </div>
        </div>
    );
}
