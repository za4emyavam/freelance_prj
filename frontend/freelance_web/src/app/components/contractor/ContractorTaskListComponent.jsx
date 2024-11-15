import {useEffect, useState} from "react";
import {useNavigate} from "react-router-dom";
import {retrieveAllContractorsTasks} from "../../api/ContractorApi";
import {formDate} from "../../js/formDate";
import Pagination from "../Pagination";

export default function ContractorTaskListComponent() {
    const [isLoading, setIsLoading] = useState(true)
    const [tasks, setTasks] = useState([])
    const [selectedStatus, setSelectedStatus] = useState("PERFORMING")
    const [orderBy, setOrderBy] = useState("")
    const [order, setOrder] = useState("asc")
    const [offset, setOffset] = useState(0)
    const [totalCount, setTotalCount] = useState(0)
    const navigator = useNavigate()

    useEffect(() => {
        refreshPage()
    }, [])

    function refreshPage() {
        setIsLoading(true)
        Promise.all([retrieveAllContractorsTasks(orderBy, order, offset, "status", selectedStatus)])
            .then((responses) => {
                setTasks(responses[0].data.tasks)
                setTotalCount(responses[0].data.totalCount)
            })
            .finally(() => setIsLoading(false))
    }

    const handleFilterChange = (event) => {
        setSelectedStatus(event.target.value)

        retrieveData(orderBy, order, 0, event.target.value)
    }

    const handleOrderBy = (value) => {
        let newOrder = ""
        if (orderBy === value) {
            if (order === "asc")
                newOrder = "desc"
            else
                newOrder = "asc"
        } else {
            newOrder = "asc"
        }
        setOrderBy(value)
        setOrder(newOrder)

        retrieveData(value, newOrder, offset, selectedStatus)
    }

    function retrieveData(orderBy, ordering, offset, selectedStatus) {
        Promise.all([retrieveAllContractorsTasks(orderBy, ordering, offset, "status", selectedStatus)])
            .then((responses) => {
                setTasks(responses[0].data.tasks)
                setTotalCount(responses[0].data.totalCount)
            })
    }

    const handlePageChange = (newOffset) => {
        setOffset(newOffset)
        retrieveData(orderBy, order, newOffset, selectedStatus)
    }

    const handleRetrieveTask = (taskId) => {
        navigator(`/contractor/tasks/${taskId}`)
    }

    if (isLoading)
        return (
            <div>

            </div>
        )

    return (
        <div className="flex justify-center mt-10">
            <div className="overflow-x-auto sm:rounded-lg w-3/4">
                <table className="w-full text-sm text-left rtl:text-right text-gray-400">
                    <caption
                        className="p-5 text-2xl font-semibold text-left rtl:text-right text-gray-800 bg-gray-300">
                        <div className="flex justify-between items-center">
                            <div>
                                All your tasks
                                <p className="mt-1 text-lg font-normal text-gray-600">
                                    Browse a list
                                    of all tasks that you are performing or performed. You can sort it by task status,
                                    cost, end date
                                    or creation date
                                </p>
                            </div>
                            <select
                                value={selectedStatus}
                                onChange={handleFilterChange}
                                className="flex-col ml-2 p-1 border rounded bg-gray-200 text-lg text-gray-700 items-end"
                            >
                                <option value="PERFORMING">PERFORMING</option>
                                <option value="DONE">DONE</option>
                                <option value="REJECTED">REJECTED</option>
                            </select>
                        </div>

                    </caption>
                    <thead className="text-lg uppercase bg-gray-700 text-gray-400">
                    <tr>
                        <th scope="col" className="px-6 py-3">
                            <div className="flex items-center">
                                <div
                                    className={orderBy === 'id_task' ? "flex items-center text-gray-300" : "flex items-center"}>
                                    Task id
                                    <button onClick={() => handleOrderBy("id_task")}>
                                        <svg className="w-4 h-4 ms-1.5" aria-hidden="true"
                                             xmlns="http://www.w3.org/2000/svg"
                                             fill="currentColor" viewBox="0 0 24 24">
                                            <path
                                                d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </th>
                        <th scope="col" className="px-6 py-3">
                            Task name
                        </th>
                        <th scope="col" className="px-6 py-3">
                            <div className="flex items-center">
                                <div
                                    className={orderBy === 'cost' ? "flex items-center text-gray-300" : "flex items-center"}>
                                    Cost
                                    <button onClick={() => handleOrderBy("cost")}>
                                        <svg className="w-4 h-4 ms-1.5" aria-hidden="true"
                                             xmlns="http://www.w3.org/2000/svg"
                                             fill="currentColor" viewBox="0 0 24 24">
                                            <path
                                                d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </th>
                        <th className="px-6 py-3">
                            Field of Activity

                        </th>
                        <th scope="col" className="px-6 py-3">
                            <div className="flex items-center">
                                <div
                                    className={orderBy === 'creation_date' ? "flex items-center text-gray-300" : "flex items-center"}>
                                    Create date
                                    <button onClick={() => handleOrderBy("creation_date")}>
                                        <svg className="w-4 h-4 ms-1.5" aria-hidden="true"
                                             xmlns="http://www.w3.org/2000/svg"
                                             fill="currentColor" viewBox="0 0 24 24">
                                            <path
                                                d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </th>
                        <th scope="col" className="px-6 py-3">
                            <div className="flex items-center">
                                <div
                                    className={orderBy === 'end_date' ? "flex items-center text-gray-300" : "flex items-center"}>
                                    End date
                                    <button onClick={() => handleOrderBy("end_date")}>
                                        <svg className="w-4 h-4 ms-1.5" aria-hidden="true"
                                             xmlns="http://www.w3.org/2000/svg"
                                             fill="currentColor" viewBox="0 0 24 24">
                                            <path
                                                d="M8.574 11.024h6.852a2.075 2.075 0 0 0 1.847-1.086 1.9 1.9 0 0 0-.11-1.986L13.736 2.9a2.122 2.122 0 0 0-3.472 0L6.837 7.952a1.9 1.9 0 0 0-.11 1.986 2.074 2.074 0 0 0 1.847 1.086Zm6.852 1.952H8.574a2.072 2.072 0 0 0-1.847 1.087 1.9 1.9 0 0 0 .11 1.985l3.426 5.05a2.123 2.123 0 0 0 3.472 0l3.427-5.05a1.9 1.9 0 0 0 .11-1.985 2.074 2.074 0 0 0-1.846-1.087Z"/>
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    {tasks.map((task) => (
                        <tr key={task.taskId}
                            className="text-lg  bg-gray-800 border-gray-700 hover:bg-gray-600"
                            onClick={() => {
                                handleRetrieveTask(task.taskId)
                            }}>
                            <th scope="row"
                                className="px-6 py-4 font-medium whitespace-nowrap text-white">
                                {task.taskId}
                            </th>
                            <th scope="row"
                                className="px-6 py-4 font-medium whitespace-nowrap text-white">
                                {task.name}
                            </th>
                            <td className="px-6 py-4">
                                {task.cost} $
                            </td>
                            <td className="px-6 py-4">
                                {task.field}
                            </td>
                            <td className="px-6 py-4">
                                {formDate(task.creationDate)[0]}/{formDate(task.creationDate)[1]}/{formDate(task.creationDate)[2]} {formDate(task.creationDate)[3]}
                            </td>
                            <td className="px-6 py-4">
                                {formDate(task.endDate)[0]}/{formDate(task.endDate)[1]}/{formDate(task.endDate)[2]}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <nav className="flex items-center flex-column flex-wrap md:flex-row justify-between pt-4"
                     aria-label="Table navigation">
                    <span
                        className="text-sm font-normal text-gray-500 ml-2 mb-4 md:mb-0 block w-full md:inline md:w-auto">Showing <span
                        className="font-semibold text-gray-900">{offset === 0 ? (totalCount < 5 ? `1-${totalCount}` : "1-5") : `${offset - 4}-${offset}`}</span> of <span
                        className="font-semibold text-gray-900">{totalCount}</span></span>
                    {totalCount > 5 && (
                        <Pagination
                            totalCount={totalCount}
                            offset={offset}
                            onPageChange={handlePageChange}/>
                    )}

                </nav>
            </div>

        </div>
    )
}