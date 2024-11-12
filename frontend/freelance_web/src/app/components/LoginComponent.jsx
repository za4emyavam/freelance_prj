import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../security/AuthContext";
import getDefRoutes from "../js/getDefRoutes";

export default function LoginComponent() {
    const [isLoading, setIsLoading] = useState(true)
    const [username, setUsername] = useState("")
    const [password, setPassword] = useState("")

    const [showErrorMessage, setShowErrorMessage] = useState(false)

    const navigate = useNavigate()

    const authContext = useAuth()

    useEffect(() => {
        checkIsAuth()
    }, []);

    function checkIsAuth() {
        setIsLoading(true)
        if (authContext.isAuthenticated) {
            navigate(getDefRoutes(localStorage.getItem('role')))
        } else {
            setIsLoading(false)
        }
    }


    function usernameOnChange(event) {
        setUsername(event.target.value)
    }

    function passwordOnChange(event) {
        setPassword(event.target.value)
    }

    async function handleSubmit() {
        if (await authContext.login(username, password)) {
            navigate(getDefRoutes(localStorage.getItem('role')))
        } else {
            setShowErrorMessage(true)
        }
    }


    if (isLoading)
        return (
            <div>

            </div>
        )

    return (
        <div className="flex flex-col mt-[150px] justify-center items-center">
            <div className="w-[640px] h-[582px] flex-col justify-center items-center gap-8 inline-flex">
                <div
                    className="px-14 py-10 rounded-3xl border border-stone-500/opacity-50 flex-col justify-start items-start gap-2.5 flex">
                    <div className="flex-col justify-center items-center gap-10 flex">
                        <div className="text-center text-zinc-800 text-[32px] font-semibold font-sans">Sign in
                        </div>
                        {showErrorMessage &&
                            <div className="errorMessage">Authentication failed. Check your credentials.</div>}
                        <div className="flex-col justify-start items-start gap-6 flex">
                            <div className="h-[87px] flex-col justify-start items-start gap-1 flex">
                                <div className="w-[528px] pr-[484px] pb-[3px] justify-start items-center inline-flex">
                                    <div className="text-stone-500 text-base font-normal font-['Poppins']">Email</div>
                                </div>
                                <div className="w-[530px] h-15 relative rounded-xl border border-stone-500/opacity-30">
                                    <input className="w-[528px] h-14 rounded-xl border text-base font-normal"
                                           type="text"
                                           name="username"
                                           value={username} onChange={usernameOnChange}/>
                                </div>
                            </div>
                            <div className="h-[87px] flex-col justify-start items-start gap-1 flex">
                                <div className="pr-[8.86px] justify-start items-start gap-[327.14px] inline-flex">
                                    <div className="text-stone-500 text-base font-normal font-['Poppins']">Password
                                    </div>
                                    <div className="w-[73px] self-stretch relative">
                                        <div
                                            className="w-6 h-6 pl-[2.91px] pr-[2.90px] py-1 left-0 top-[3px] absolute justify-center items-center inline-flex">
                                            <div className="w-[18.19px] h-4 relative">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="w-[530px] h-15 relative rounded-xl border border-stone-500/opacity-30">
                                    <input className="w-[528px] h-14 relative rounded-xl border" type="password"
                                           name="password"
                                           value={password} onChange={passwordOnChange}/>
                                </div>
                            </div>
                            <div className="flex-col justify-center items-center gap-2 flex">
                                <button
                                    className="w-[528px] h-16 pt-[15px] pb-4 opacity-25 bg-neutral-900 rounded-[40px] justify-center items-center inline-flex"
                                    type="button" name="Login" onClick={handleSubmit}>
                                    <div className="justify-center items-center gap-2 inline-flex">
                                        <div
                                            className="text-center text-white text-[22px] font-semibold font-sans">Log in
                                        </div>
                                    </div>
                                </button>
                                <div className="pr-2 py-2 justify-start items-start gap-2.5 inline-flex">
                                    <div></div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}