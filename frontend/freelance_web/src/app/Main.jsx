import {BrowserRouter, Navigate, Outlet, Route, Routes} from "react-router-dom";
import AuthProvider, {useAuth} from "./security/AuthContext";
import LoginComponent from "./components/LoginComponent";
import TasksComponent from "./components/customer/TasksComponent";
import getDefRoutes from "./js/getDefRoutes";
import LogoutComponent from "./components/LogoutComponent";
import HeaderComponent from "./components/HeaderComponent";
import TaskComponent from "./components/customer/TaskComponent";
import NoContentComponent from "./components/NoContentComponent";
import CreateTask from "./components/customer/CreateTask";
import ProfileComponent from "./components/ProfileComponent";
import UpdateProfileComponent from "./components/UpdateProfileComponent";
import TasksListComponent from "./components/contractor/TasksListComponent";
import ContractorTaskComponent from "./components/contractor/ContractorTaskComponent";
import ContractorTaskListComponent from "./components/contractor/ContractorTaskListComponent";
import RegistrationComponent from "./components/RegistrationComponent";
import StatisticComponent from "./components/admin/StatisticComponent";
import FieldsComponent from "./components/admin/FieldsComponent";
import FieldComponent from "./components/admin/FieldComponent";

function AuthenticatedRoute() {
    const authContext = useAuth()

    try {
        if (authContext.isAuthenticated)
            return <Outlet/>
        else {
            authContext.logout()
            return <Navigate to="/"/>
        }

    } catch (error) {
        authContext.logout()
        return <Navigate to="/"/>
    }
}

function RoleRoute({role}) {
    if (role === localStorage.getItem("role")) {
        return <Outlet/>
    } else {
        return <Navigate to={getDefRoutes()}/>
    }
}

export default function Main() {
    return (
        <div className="App">
            <AuthProvider>
                <BrowserRouter>
                    <HeaderComponent/>
                    <Routes>
                        <Route path="/" element={<LoginComponent/>}/>
                        <Route path="/registration" element={<RegistrationComponent/>}/>
                        <Route path="/customer" element={<RoleRoute role={'CUSTOMER'}/>}>
                            <Route path="tasks" element={<TasksComponent/>}/>
                            <Route path="tasks/:taskId" element={<TaskComponent/>}/>
                            <Route path="tasks/create" element={<CreateTask/>}/>
                            <Route path="tasks/:taskId/update" element={<CreateTask/>}/>
                            <Route path="profile" element={<ProfileComponent/>}/>
                            <Route path="profile/update" element={<UpdateProfileComponent/>}/>
                        </Route>
                        <Route path="/contractor" element={<RoleRoute role={'CONTRACTOR'}/>}>
                            <Route path="tasks" element={<TasksListComponent/>}/>
                            <Route path="tasks/:taskId" element={<ContractorTaskComponent/>}/>
                            <Route path="your_tasks" element={<ContractorTaskListComponent/>}/>
                            <Route path="profile" element={<ProfileComponent/>}/>
                            <Route path="profile/update" element={<UpdateProfileComponent/>}/>
                        </Route>
                        <Route path="/admin" element={<RoleRoute role={'ADMIN'}/>}>
                            <Route path="statistic" element={<StatisticComponent/>}/>
                            <Route path="fields" element={<FieldsComponent/>}/>
                            <Route path="fields/:fieldId" element={<FieldComponent/>}/>
                            <Route path="fields/create" element={<FieldComponent/>}/>
                        </Route>
                        <Route path="logout" element={<AuthenticatedRoute/>}>
                            <Route index element={<LogoutComponent/>}/>
                        </Route>
                        <Route path="error" element={<NoContentComponent/>}/>
                    </Routes>
                </BrowserRouter>
            </AuthProvider>
        </div>
    )
}