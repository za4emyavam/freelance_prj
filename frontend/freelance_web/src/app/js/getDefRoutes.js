export default function getDefRoutes() {
    switch (localStorage.getItem("role")) {
        case "CUSTOMER":
            return "/customer/tasks"
        case "CONTRACTOR":
            return "/contractor/tasks"
        case "ADMIN":
            return "/temp"
        default:
            return "/"
    }
}