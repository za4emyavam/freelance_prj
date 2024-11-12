import {Navigate} from "react-router-dom";
import {useAuth} from "../security/AuthContext";

export default function LogoutComponent() {
    const authContext = useAuth()
    authContext.logout()

    return <Navigate to="/"/>
}