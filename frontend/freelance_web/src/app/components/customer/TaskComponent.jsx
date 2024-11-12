import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {
    acceptContractor, acceptTask, cancelTask,
    getContractorById,
    rejectContractorByTaskId,
    retrieveCustomerTaskByTaskId
} from "../../api/CustomerApi";
import {formDate} from "../../js/formDate";
import {ErrorMessage, Field, Form, Formik} from "formik";
import * as Yup from "yup";

export default function TaskComponent() {
    const {taskId} = useParams()
    const [task, setTask] = useState(null)
    const [isLoading, setIsLoading] = useState(true)
    const [isOpenReject, setIsOpenReject] = useState(false)
    const [isOpenProfile, setIsOpenProfile] = useState(false)
    const [isOpenCancel, setIsOpenCancel] = useState(false)
    const [isOpenAccept, setIsOpenAccept] = useState(false)
    const [contractor, setContractor] = useState(null)
    const navigator = useNavigate()

    useEffect(() => {
        refreshTask()
    }, [])

    const toggleModalReject = () => {
        setIsOpenReject(!isOpenReject);
    }

    const toggleModalCancel = () => {
        setIsOpenCancel(!isOpenCancel);
    }

    const toggleModalAccept = () => {
        setIsOpenAccept(!isOpenAccept);
    }

    const toggleModalProfile = (contractorId) => {
        if (isOpenProfile) {
            setIsOpenProfile(false)
            setContractor(null)
        } else {
            Promise.all([getContractorById(contractorId)])
                .then((response) => {
                    setContractor(response[0].data)
                }).finally(() => setIsOpenProfile(true))
        }

    }

    const handleAcceptContractor = (contractorId) => {
        Promise.all([acceptContractor(taskId, contractorId)])
            .then((response) => {
                setIsOpenProfile(false)
                refreshTask()
            })
    }

    const handleCancelTask = () => {
        Promise.all([cancelTask(taskId)])
            .then((response) => {
                    setIsOpenCancel(false)
                    refreshTask()
                }
            )
    }

    const handleAcceptTask = (values) => {
        Promise.all([acceptTask(taskId, values.rate)])
            .then((response) => {
                    setIsOpenAccept(false)
                    refreshTask()
                }
            )
    }

    const handleUpdate = () => {
        navigator(`/customer/tasks/${taskId}/update`)
    }

    function refreshTask() {
        setIsLoading(true)
        Promise.all([
            retrieveCustomerTaskByTaskId(taskId)
        ]).then((responses) => {
            setTask(responses[0].data)
        }).catch((error) => {
            if (error.response.data === "")
                return navigator(`/error`)
        })
            .finally(() => setIsLoading(false))
    }

    const validationSchema = Yup.object().shape({
        reason: Yup.string().required("Required")
    });

    const validationAccept = Yup.object().shape({
        rate: Yup.string().matches(/^[1-5]$/,"Must be a number from 1 to 5")
    });

    function rejectContractor(values) {
        const contractorId = task.contractors.find((contractor) => contractor.status === 'APPROVED').userId
        toggleModalReject()
        Promise.all([rejectContractorByTaskId(taskId, contractorId, values.reason)])
            .then(refreshTask)
    }

    if (isLoading) {
        return (
            <div></div>
        )
    }

    return (
        <div className="flex flex-col">
            <div className="flex p-4 rounded-lg">
                <div className="w-[386.65px] h-[397px] left-20  relative">
                    <div className="w-[386.65px] h-[397px] left-0 top-0 absolute">
                        <div
                            className="w-[386.65px] h-[397px] left-0 top-0 absolute bg-blue-100 rounded-[40px] shadow"/>
                        <div className="w-[386.65px] h-[397px] left-0 top-0 absolute">
                            <div
                                className="w-[386.65px] h-[397px] left-0 top-0 absolute bg-stone-600 rounded-[40px] shadow"/>
                            <div className="w-[427.16px] h-[397.86px] left-[-20.71px] top-0 absolute"/>
                        </div>
                        <div
                            className="w-[386.65px] h-[397px] left-0 top-0 absolute opacity-10 bg-slate-900 rounded-[40px] shadow"/>
                        <div
                            className="w-[386.65px] h-[397px] left-0 top-0 absolute bg-slate-900 rounded-[40px] shadow"/>
                    </div>
                    <div
                        className="w-[243.96px] h-[41.25px] left-[71.35px] top-[160.88px] absolute text-center text-white text-[37px] font-black font-['Nunito Sans'] leading-[48px]">
                        {task.name}
                    </div>
                    <div
                        className="w-[204px] h-[17px] left-[21px] top-[35px] absolute text-white text-xl font-bold font-['Nunito Sans'] leading-tight">
                        {task.fieldOfActivity.field}
                    </div>
                    {(task.status === 'ACTIVE' || task.status === 'PERFORMING') && (
                        <div
                            className="absolute text-center top-[400px] left-[120px] ">
                            {task.status === 'PERFORMING' && (
                                <div
                                    className="relative w-[150px] mt-3 font-semibold text-lg bg-green-500 dark:bg-green-500 rounded text-gray-100 hover:bg-green-600"
                                    onClick={toggleModalAccept}>
                                    <p className="p-2">
                                        Accept task
                                    </p>
                                </div>
                            )}

                            <div
                                className="relative w-[150px] mt-3 font-semibold text-lg bg-blue-500 dark:bg-blue-500 rounded text-gray-50 hover:bg-blue-600"
                                onClick={handleUpdate}>
                                <p className="p-2">
                                    Update task
                                </p>
                            </div>

                            <div
                                className="relative w-[150px] mt-3 font-semibold text-lg bg-red-500 dark:bg-red-500 rounded text-gray-50 hover:bg-red-600"
                                onClick={toggleModalCancel}>
                                <p className="p-2">
                                    Cancel task
                                </p>
                            </div>
                        </div>
                    )}
                </div>


                <div className="flex flex-col w-3/4 ml-4">
                    <div className="flex justify-between p-6 bg-gray-200 rounded-lg">
                        <div className="flex inline">
                            <span className="text-xl ml-32 font-semibold text-gray-700 mt-1">Task status: </span>
                            <div className="ml-3">
                                {task.status === 'PERFORMING' ?
                                    <div
                                        className="text-center font-semibold bg-purple-500 dark:bg-purple-500 rounded w-[130px] text-purple-950">
                                        <p className="p-2">
                                            {task.status}
                                        </p>
                                    </div>
                                    : task.status === 'DONE' ?
                                        <div
                                            className="text-center w-[130px] font-semibold bg-green-500 dark:bg-green-500 rounded text-green-950">
                                            <p className="p-2">
                                                {task.status}
                                            </p>
                                        </div>
                                        : task.status === 'ACTIVE' ?
                                            <div
                                                className="text-center w-[130px] font-semibold bg-blue-500 dark:bg-blue-500 rounded text-blue-950">
                                                <p className="p-2">
                                                    {task.status}
                                                </p>
                                            </div>
                                            : <div
                                                className="text-center w-[130px] font-semibold bg-red-500 dark:bg-red-500 rounded text-red-950">
                                                <p className="p-2">
                                                    {task.status}
                                                </p>
                                            </div>}
                            </div>
                        </div>

                        <span
                            className="text-gray-500 mt-1">End date: {formDate(task.endDate)[0]} {formDate(task.endDate)[1]} {formDate(task.endDate)[2]}</span>
                    </div>
                    <div className="ml-20">

                        <div className="mt-2 p-3 dark:bg-gray-800 dark:border-gray-700 rounded-lg text-gray-200">
                            <div className="text-right">
                                Date of creation:
                                <span className="text-gray-300 ml-1">
                                    {formDate(task.creationDate)[0]} {formDate(task.creationDate)[1]} {formDate(task.creationDate)[2]} {formDate(task.creationDate)[3]}
                                </span>
                            </div>
                            <span className=" flex text-lg font-semibold text-left">Description:</span>
                            <p className="mt-1 text-gray-300 text-left">
                                {task.description}
                            </p>

                            {task.links && (
                                <div className="text-left">
                                    <span className="mt-2 flex text-lg font-semibold ">Links:</span>
                                    {task.links.map((link) => (
                                        <a href={link.link} key={link.link} className="text-gray-300">
                                            {link.link}
                                        </a>
                                    ))}
                                </div>
                            )}
                            {(task.status === 'PERFORMING' || task.status === 'DONE') && (
                                <div className="text-left flex inline">
                                    <span className="mt-2 flex text-lg font-semibold ">Date of start:</span>
                                    <span className="mt-2 ml-2 flex text-lg text-gray-300">
                                        {formDate(task.startDate)[0]} {formDate(task.startDate)[1]} {formDate(task.startDate)[2]} {formDate(task.startDate)[3]}
                                    </span>
                                </div>
                            )}
                            {(task.status === 'DONE') && (
                                <div className="text-left flex inline">
                                    <span className="mt-2 flex text-lg font-semibold ">Date of finish:</span>
                                    <span className="mt-2 ml-2 flex text-lg text-gray-300">
                                        {formDate(task.finishedDate)[0]} {formDate(task.finishedDate)[1]} {formDate(task.finishedDate)[2]} {formDate(task.finishedDate)[3]}
                                    </span>
                                </div>
                            )}
                        </div>
                    </div>
                    {(task.status === 'DONE' || task.status === 'PERFORMING') &&
                        <RequestedContractor
                            contractor={task.contractors.find((contractor) => contractor.status === 'APPROVED')}
                            status={task.status} toggleModal={toggleModalReject}/>}
                    {task.status === 'ACTIVE' &&
                        <ListRequestedContractors className="ml-32" contractors={task.contractors} navigator={navigator}
                                                  taskId={taskId} toggleModal={toggleModalProfile}/>}
                </div>
            </div>

            {/*Modal Window to reject user*/}
            {
                isOpenReject && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                        <div className="relative w-full max-w-md p-6 bg-white rounded-lg shadow-lg">
                            <button
                                className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                                onClick={toggleModalReject}
                            >
                                ✖
                            </button>

                            <h2 className="mb-4 text-2xl font-semibold text-center">Reject contractor</h2>

                            <Formik
                                initialValues={{reason: ""}}
                                validationSchema={validationSchema}
                                onSubmit={(values) => rejectContractor(values)}
                            >
                                {({isSubmitting}) => (
                                    <Form>

                                        <div className="mb-4">
                                            <label htmlFor="reason" className="block mb-1 font-semibold">Reason</label>
                                            <Field
                                                as="input"
                                                name="reason"
                                                className="w-full p-2 border rounded"
                                                rows="3"
                                            />
                                            <ErrorMessage name="reason" component="div" className="text-red-500"/>
                                        </div>

                                        <div className="flex justify-center">
                                            <button
                                                type="submit"
                                                disabled={isSubmitting}
                                                className="px-4 py-2 text-white bg-green-500 rounded hover:bg-green-600"
                                            >
                                                Submit
                                            </button>
                                        </div>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </div>
                )
            }

            {/*Modal Windows for contractor profile*/}
            {
                isOpenProfile && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                        <div className="relative w-full max-w-lg p-6 bg-white rounded-lg shadow-lg">
                            <button
                                className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                                onClick={toggleModalProfile}
                            >
                                ✖
                            </button>

                            <h2 className="mb-4 text-2xl font-semibold text-center">Contractor Profile</h2>

                            <div className="mb-4">
                                <label className="font-semibold">Full name:</label>
                                <p className="text-gray-700">{contractor.name} {contractor.secondName} {contractor.surname}</p>
                            </div>

                            <div className="mb-4">
                                <label className="font-semibold">Email:</label>
                                <p className="text-gray-700">{contractor.email}</p>
                            </div>

                            <div className="mb-4">
                                <label className="font-semibold">Raiting:</label>
                                <p className="text-yellow-500">{contractor.rating} ⭐</p>
                            </div>

                            <div className="mb-4">
                                <label className="font-semibold">Description:</label>
                                <p className="text-gray-700">{contractor.about}</p>
                            </div>

                            <div className="mb-4">
                                <label className="font-semibold">The number of completed tasks in various fields:</label>

                                {contractor.fieldRatingList.length === 0 ? (
                                    <p>At the moment, this contractor has not yet completed any tasks, but still give him a
                                        chance.</p>
                                ) : (<table className="w-full mt-2 border">
                                    <thead>
                                    <tr>
                                        <th className="px-4 py-2 border-b">Field of Activity</th>
                                        <th className="px-4 py-2 border-b">Amount</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    {contractor.fieldRatingList.map((task, index) => (
                                        <tr key={index}>
                                            <td className="px-4 py-2 border-b">{task.name}</td>
                                            <td className="px-4 py-2 border-b">{task.taskCompleted}</td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>)}

                            </div>
                            <div className="flex justify-between mt-4">
                                <div className="ml-3">
                                    <button
                                        onClick={() => handleAcceptContractor(contractor.userId)}
                                        className="px-4 py-2 text-white bg-green-500 rounded hover:bg-green-600"
                                    >
                                        Accept contractor
                                    </button>
                                </div>

                                <div className="mr-3">
                                    <button
                                        onClick={toggleModalProfile}
                                        className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600"
                                    >
                                        Close
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )
            }

            {/*Modal Windows for cancelling task*/}
            {
                isOpenCancel && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                        <div className="relative w-full max-w-lg p-6 bg-white rounded-lg shadow-lg">
                            <button
                                className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                                onClick={toggleModalCancel}
                            >
                                ✖
                            </button>

                            <h2 className="mb-4 text-2xl font-semibold text-center">Cancelling task</h2>
                            <p>
                                Are you sure you want to cancel the task?
                            </p>
                            <div className="flex justify-between mt-4">
                                <div className="ml-3">
                                    <button
                                        onClick={() => handleCancelTask()}
                                        className="px-4 py-2 text-white bg-red-500 rounded hover:bg-red-600"
                                    >
                                        Cancel task
                                    </button>
                                </div>

                                <div className="mr-3">
                                    <button
                                        onClick={toggleModalCancel}
                                        className="px-4 py-2 text-white bg-blue-500 rounded hover:bg-blue-600"
                                    >
                                        Close
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                )
            }

            {/*Modal Window to accept task*/}
            {
                isOpenAccept && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                        <div className="relative w-full max-w-md p-6 bg-white rounded-lg shadow-lg">
                            <button
                                className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                                onClick={toggleModalReject}
                            >
                                ✖
                            </button>

                            <h2 className="mb-4 text-2xl font-semibold text-center">Rate contractor work</h2>

                            <Formik
                                initialValues={{rate: 5}}
                                validationSchema={validationAccept}
                                onSubmit={(values) => handleAcceptTask(values)}
                            >
                                {({isSubmitting}) => (
                                    <Form>

                                        <div className="mb-4">
                                            <label htmlFor="rate" className="block mb-1 font-semibold text-left">Rate <span className="font-normal text-gray-700 text-xs">(from 1 to 5)</span></label>
                                            <Field
                                                as="input"
                                                name="rate"
                                                className="w-full p-2 border rounded"
                                                rows="3"
                                            />
                                            <ErrorMessage name="rate" component="div" className="text-red-500"/>
                                        </div>

                                        <div className="flex justify-center">
                                            <button
                                                type="submit"
                                                disabled={isSubmitting}
                                                className="px-4 py-2 text-white bg-green-500 rounded hover:bg-green-600"
                                            >
                                                Submit
                                            </button>
                                        </div>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </div>
                )
            }
        </div>
    )
}

const RequestedContractor = ({contractor, status, toggleModal}) => {
    return (
        <div className="w-fit bg-gray-500 mt-3 ml-20 rounded-lg p-5 text-lg">
            <span className="text-center font-semibold text-gray-100">Contractor who performing your task:</span>
            <div className="mt-2 flex inline">
                <span className="text-gray-100">Email:</span>
                <span className="ml-2 text-gray-200">{contractor.email}</span>
            </div>
            <div className="mt-2 flex inline">
                <span className="text-gray-100">Full name:</span>
                <span
                    className="ml-2 text-gray-200">{contractor.name} {contractor.secondName} {contractor.surname}</span>
            </div>
            <div className="mt-2 flex inline">
                <span className="text-gray-100">Rating:</span>
                <span
                    className="ml-2 text-gray-200">{contractor.rating}</span>
            </div>
            <div className="mt-2 mb-3 flex inline">
                <span className="text-gray-100">Task completed:</span>
                <span
                    className="ml-2 text-gray-200">{contractor.taskCompleted}</span>
            </div>
            {status === 'PERFORMING' && (
                <div onClick={toggleModal} className="text-center mt-5 px-4 py-2 text-gray-200 bg-red-900 rounded
                                   hover:bg-red-950 hover:text-white">
                    Reject contractor
                </div>
            )}
        </div>
    )
}

const ListRequestedContractors = ({contractors, navigator, taskId, toggleModal}) => {
    const handleClick = (contractorId) => {
        toggleModal(contractorId)
    }

    return (
        <div className="ml-20 mt-5">
            <table className="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400 ">
                <caption
                    className="p-5 text-2xl font-semibold text-left rtl:text-right text-gray-800 dark:bg-gray-300 rounded-t-lg">

                    <div className="flex justify-between items-center">
                        <div>
                            Contractors
                            <p className="mt-1 text-lg font-normal text-gray-600 dark:text-gray-600">
                                Browse a list of contractors who responded to your task.
                            </p>
                        </div>
                    </div>

                </caption>
                <thead className="text-lg text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
                <tr>
                    <th scope="col" className="px-6 py-3">
                        Email
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Full name
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Rating
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Completed task
                    </th>
                    <th scope="col" className="px-6 py-3">
                        Status
                    </th>

                </tr>
                </thead>
                <tbody>
                {contractors.map((contractor) => (
                    <tr key={contractor.userId}
                        className="text-lg bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
                        onClick={() => handleClick(contractor.userId)}
                    >
                        {/*onClick={() => handleTaskClick(task.idTask)}*/}
                        <th scope="row"
                            className="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
                            {contractor.email}
                        </th>
                        <td className="px-6 py-4">
                            {contractor.name} {contractor.secondName} {contractor.surname}
                        </td>
                        <td className="px-6 py-4">
                            {contractor.rating}
                        </td>
                        <td className="px-6 py-4">
                            {contractor.taskCompleted}
                        </td>
                        <td className="px-6 py-4">
                            {contractor.status}
                        </td>
                    </tr>
                ))}
                </tbody>
                {contractors.length === 0 && (
                    <span className="text-[16px] ml-2 p-2 w-full">At the moment, no contractor has responded to this task yet. Please wait a little longer.</span>
                )}
            </table>
        </div>
    )
}