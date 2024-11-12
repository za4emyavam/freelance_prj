import {useEffect, useState} from "react";
import {agreedToTask, retrieveContractorTaskById} from "../../api/ContractorApi";
import {useParams} from "react-router-dom";
import {formDate} from "../../js/formDate";
import {ErrorMessage, Field, Form, Formik} from "formik";

export default function ContractorTaskComponent() {
    const [task, setTask] = useState("")
    const [isLoading, setIsLoading] = useState(true)
    const {taskId} = useParams()
    const [isRequestTask, setIsRequestTask] = useState(false)

    useEffect(() => {
        refreshPage()
    }, [])

    function refreshPage() {
        setIsLoading(true)
        Promise.all([retrieveContractorTaskById(taskId)])
            .then((responses) => {
                setTask(responses[0].data)
            })
            .finally(() => setIsLoading(false))
    }

    const toggleModalRequest = () => {
        setIsRequestTask(!isRequestTask)
    }

    const handleRequest = () => {
        if (task.contractorStatus === null) {
            setIsLoading(true)
            setIsRequestTask(false)
            Promise.all([agreedToTask(taskId)])
                .finally(() =>
                    refreshPage()
                )
        }
    }

    if (isLoading) {
        return (
            <div>

            </div>
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
                        {task.taskName}
                    </div>
                    <div
                        className="w-[204px] h-[17px] left-[21px] top-[35px] absolute text-white text-xl font-bold font-['Nunito Sans'] leading-tight">
                        {task.field}
                    </div>
                        <div
                            className="absolute text-center top-[400px] ">
                            {(task.status === 'ACTIVE' && task.contractorStatus === null) && (
                                <div
                                    className="relative left-[120px] w-[150px] mt-3 font-semibold text-lg bg-green-500 dark:bg-green-500 rounded text-gray-100 hover:bg-green-600"
                                    onClick={toggleModalRequest}>
                                    <p className="p-2">
                                        Request to task
                                    </p>
                                </div>
                            )}
                            {task.contractorStatus !== null && (
                                <div className="flex inline">
                                    <p className="font-bold text-xl mt-3 ml-4 text-gray-700">Request status:</p>
                                    {task.contractorStatus === 'AGREED' ?
                                        <div
                                            className="relative w-[150px] ml-3 mt-2 font-semibold text-lg bg-blue-500 rounded text-gray-100">
                                            <p className="p-2">
                                                {task.contractorStatus}
                                            </p>
                                        </div>
                                        :
                                        task.contractorStatus === 'APPROVED' ?
                                            <div
                                                className="relative w-[150px] ml-3 mt-2 font-semibold text-lg bg-green-500 rounded text-gray-100">
                                                <p className="p-2">
                                                    {task.contractorStatus}
                                                </p>
                                            </div>
                                            :
                                            <div
                                                className="relative w-[150px] ml-3 mt-2 font-semibold text-lg bg-red-500 rounded text-gray-100">
                                                <p className="p-2">
                                                    {task.contractorStatus}
                                                </p>
                                            </div>

                                    }

                                </div>
                            )}
                        </div>
                </div>


                <div className="flex flex-col w-3/4 ml-4">
                    <div className="flex justify-between p-6 bg-gray-200 rounded-lg">
                        {task.reason === null ? (
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
                        ) : (<div className="flex">

                        </div>)}


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
                            <span className=" flex text-lg font-semibold text-left">Cost:
                                <p className="text-gray-300 text-left ml-1 font-normal">
                                {task.cost} $
                                </p>
                            </span>

                            <span className="flex text-lg font-semibold text-left">Description:</span>
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
                            <span className="flex text-lg font-bold text-left mt-3">Customer details:</span>
                            <span className="flex text-lg font-semibold text-left">Full name:
                                <p className="text-gray-300 text-left ml-1 font-normal">
                                    {task.name} {task.secondName} {task.surname}
                                </p>
                            </span>
                            <span className="flex text-lg font-semibold text-left">Email:
                                <p className="text-gray-300 text-left ml-1 font-normal">
                                    {task.email}
                                </p>
                            </span>

                            <span className="flex text-lg font-semibold text-left">Phone number:
                                <p className="text-gray-300 text-left ml-1 font-normal">
                                    {task.phoneNumber}
                                </p>
                            </span>
                            {task.contractorStatus !== null && (
                                <span className="flex text-lg font-semibold text-left">Request to complete task submitted on:
                                <p className="text-gray-300 text-left ml-1 font-normal">
                                    {formDate(task.feedbackDate)[0]} {formDate(task.feedbackDate)[1]} {formDate(task.feedbackDate)[2]}
                                </p>
                            </span>
                            )}
                        </div>
                    </div>
                    {task.reason !== null && (
                        <div className="w-fit bg-red-200 mt-3 ml-20 rounded-lg p-5 text-lg">
                        <span
                            className="text-center font-bold text-gray-700">You have been rejected from this task</span>
                            <div className="mt-2 flex inline">
                                <span className="font-semibold text-gray-700">Reason:</span>
                                <span className="ml-2 text-gray-800">{task.reason}</span>
                            </div>
                            <div className="mt-2 flex inline">
                                <span className="font-semibold text-gray-700">Recall date:</span>
                                <span className="ml-2 text-gray-800">
                                {formDate(task.recallDate)[0]} {formDate(task.recallDate)[1]} {formDate(task.recallDate)[2]}
                            </span>
                            </div>
                        </div>
                    )}
                    {/*Modal Windows for cancelling task*/}
                    {
                        isRequestTask && (
                            <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                                <div className="relative w-full max-w-lg p-6 bg-white rounded-lg shadow-lg">
                                    <button
                                        className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                                        onClick={toggleModalRequest}
                                    >
                                        ✖
                                    </button>

                                    <h2 className="mb-4 text-2xl font-semibold text-center">Request to task</h2>
                                    <p>
                                        Are you sure you want to submit a request to complete this task?
                                    </p>
                                    <div className="flex justify-between mt-4">
                                        <div className="ml-3">
                                            <button
                                                onClick={() => handleRequest()}
                                                className="px-4 py-2 text-white bg-green-500 rounded hover:bg-green-600"
                                            >
                                                Submit request
                                            </button>
                                        </div>

                                        <div className="mr-3">
                                            <button
                                                onClick={toggleModalRequest}
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
                </div>
            </div>


        </div>
    )
}