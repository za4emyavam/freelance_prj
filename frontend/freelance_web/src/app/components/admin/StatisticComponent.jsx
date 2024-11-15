import {useEffect, useState} from "react";
import {
    retrieveAvgRating,
    retrieveCancelledTasks,
    retrieveDoneTasks,
    retrieveNotDoneTasks,
    retrieveUsersCount
} from "../../api/AdminApi";

export default function StatisticComponent() {
    const [countDoneTask, setCountDoneTask] = useState(0)
    const [countRegisteredUsers, setCountRegisteredUsers] = useState(0)
    const [countNotDoneTask, setCountNotDoneTask] = useState(0)
    const [countCancelledTask, setCountCancelledTask] = useState(0)
    const [avgRating, setAvgRating] = useState(0)
    const [dateFrom, setDateFrom] = useState(new Date(new Date().getDate() - 7))
    const [dateTo, setDateTo] = useState(new Date())
    const [isLoading, setIsLoading] = useState(true)

    useEffect(() => {
        refreshPage()
    }, []);

    function refreshPage() {
        setIsLoading(true)

        const period = {
            from: dateFrom.toJSON().slice(0, 10),
            to: dateTo.toJSON().slice(0, 10)
        }
        Promise.all([retrieveUsersCount(period),
            retrieveDoneTasks(period),
            retrieveCancelledTasks(period),
            retrieveNotDoneTasks(period),
            retrieveAvgRating()
        ])
            .then((responses) => {
                setCountRegisteredUsers(responses[0].data)
                setCountDoneTask(responses[1].data)
                setCountCancelledTask(responses[2].data)
                setCountNotDoneTask(responses[3].data)
                setAvgRating(responses[4].data)
            })
            .finally(() => setIsLoading(false))
    }

    const handleDateFromChange = (e) => {
        const date = e.target.value
        setDateFrom(date)
        retrieveData(date, dateTo)
    }

    const handleDateToChange = (e) => {
        const date = e.target.value
        setDateTo(date)
        retrieveData(dateFrom, date)
    }

    const retrieveData = (from, to) => {
        setIsLoading(true)
        const period = {
            from: from,
            to: to
        }

        Promise.all([
            retrieveUsersCount(period),
            retrieveDoneTasks(period),
            retrieveCancelledTasks(period),
            retrieveNotDoneTasks(period)
        ])
            .then((responses) => {
                setCountRegisteredUsers(responses[0].data)
                setCountDoneTask(responses[1].data)
                setCountCancelledTask(responses[2].data)
                setCountNotDoneTask(responses[3].data)
            })
            .finally(() => setIsLoading(false))
    }

    if (isLoading)
        return (
            <div>

            </div>
        )

    return (
        <div className="min-h-screen bg-gray-100 flex items-center justify-center p-4">
            <div className="bg-white rounded-lg shadow-lg p-8 max-w-2xl w-full">
                <h2 className="text-2xl font-semibold text-gray-700 mb-6 text-center">Dashboard</h2>

                <div className="grid grid-cols-2 gap-4 mb-8">
                    <div>
                        <label className="block text-sm font-medium text-gray-600 mb-1" htmlFor="dateFrom">Date from</label>
                        <input
                            type="date"
                            id="dateFrom"
                            value={dateFrom}
                            onChange={(e) => handleDateFromChange(e)}
                            className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring focus:ring-indigo-200"
                        />
                    </div>
                    <div>
                        <label className="block text-sm font-medium text-gray-600 mb-1" htmlFor="dateTo">Date to</label>
                        <input
                            type="date"
                            id="dateTo"
                            value={dateTo}
                            onChange={(e) => handleDateToChange(e)}
                            className="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring focus:ring-indigo-200"
                        />
                    </div>
                </div>

                {/* Карточки с метриками */}
                <div className="grid grid-cols-2 gap-4">
                    <div className="p-4 bg-indigo-100 rounded-lg text-center shadow-md">
                        <h3 className="text-lg font-semibold text-indigo-700">Registered users</h3>
                        <p className="text-3xl font-bold text-indigo-800">{countRegisteredUsers}</p>
                    </div>

                    <div className="p-4 bg-green-100 rounded-lg text-center shadow-md">
                        <h3 className="text-lg font-semibold text-green-700">Done tasks</h3>
                        <p className="text-3xl font-bold text-green-800">{countDoneTask}</p>
                    </div>

                    <div className="p-4 bg-yellow-100 rounded-lg text-center shadow-md">
                        <h3 className="text-lg font-semibold text-yellow-700">Uncompleted tasks</h3>
                        <p className="text-3xl font-bold text-yellow-800">{countNotDoneTask}</p>
                    </div>

                    <div className="p-4 bg-red-100 rounded-lg text-center shadow-md">
                        <h3 className="text-lg font-semibold text-red-700">Cancelled tasks</h3>
                        <p className="text-3xl font-bold text-red-800">{countCancelledTask}</p>
                    </div>

                    <div className="col-span-2 p-4 bg-blue-100 rounded-lg text-center shadow-md">
                        <h3 className="text-lg font-semibold text-blue-700">Average rating</h3>
                        <p className="text-3xl font-bold text-blue-800">{avgRating.toFixed(2)}</p>
                    </div>
                </div>
            </div>
        </div>
    )
}