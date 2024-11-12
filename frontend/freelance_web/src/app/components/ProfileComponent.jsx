import {useEffect, useState} from "react";
import {deactivateProfile, retrieveUserInfo} from "../api/CustomerApi";
import {formDate} from "../js/formDate";
import {useNavigate} from "react-router-dom";
import {deactivateContractorProfile, getContractorProfile} from "../api/ContractorApi";

export default function ProfileComponent() {
    const [user, setUser] = useState(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isDeactivated, setIsDeactivated] = useState(false);
    const navigator = useNavigate()

    useEffect(() => {
        refreshPage()
    }, [])

    function refreshPage() {
        setIsLoading(true)
        setIsDeactivated(false)
        if (localStorage.getItem('role') === "CUSTOMER") {
            Promise.all([retrieveUserInfo()])
                .then((responses) => {
                    setUser(responses[0].data);
                }).finally(() => setIsLoading(false))
        }
        if (localStorage.getItem('role') === "CONTRACTOR") {
            Promise.all([getContractorProfile()])
                .then((responses) => {
                    setUser(responses[0].data);
                }).finally(() => setIsLoading(false))
        }

    }

    const toggleDeactivate = () => {
        setIsDeactivated(!isDeactivated)
    }

    const handleDeactivateProfile = () => {
        if (localStorage.getItem('role') === "CUSTOMER") {
            Promise.all([deactivateProfile()])
                .then((response) => {
                    navigator(`/logout`)
                })
        }
        if (localStorage.getItem('role') === "CONTRACTOR") {
            Promise.all([deactivateContractorProfile()])
                .then((response) => {
                    navigator(`/logout`)
                })
        }
    }

    const handleUpdate = () => {
        if (localStorage.getItem('role') === 'CUSTOMER') {
            navigator(`/customer/profile/update`)
        }
        if (localStorage.getItem('role') === 'CONTRACTOR') {
            navigator(`/contractor/profile/update`)
        }
    }

    if (isLoading)
        return (
            <div>

            </div>
        )

    return (
        <div>
            <div className="flex flex-col justify-center items-center mt-10">
                <div className="w-1/4 p-8 bg-gray-100 rounded-lg">


                    <h2 className="mb-4 text-2xl font-semibold text-center">Your Profile</h2>
                    <div className="mb-4">
                        <label className="font-semibold">Email:</label>
                        <p className="text-gray-700">{user.email}</p>
                    </div>

                    <div className="mb-4">
                        <label className="font-semibold">Full name:</label>
                        <p className="text-gray-700">{user.name} {user.secondName} {user.surname}</p>
                    </div>

                    <div className="mb-4">
                        <label className="font-semibold">Phone number:</label>
                        <p className="text-gray-700">{user.phoneNum}</p>
                    </div>


                    {user.rating && (
                        <div className="mb-4">
                            <label className="font-semibold">Raiting:</label>
                            <p className="text-yellow-500">{user.rating} ⭐</p>
                        </div>
                    )}

                    <div className="mb-4">
                        <label className="font-semibold">Birthday date:</label>
                        <p className="text-gray-700">{formDate(user.birthday)[0]} {formDate(user.birthday)[1]} {formDate(user.birthday)[2]}</p>
                    </div>

                    <div className="mb-4">
                        <label className="font-semibold">Gender:</label>
                        <p className="text-gray-700">{user.gender}</p>
                    </div>

                    <div className="mb-4">
                        <label className="font-semibold">About me:</label>
                        <p className="text-gray-700">{user.aboutMe}</p>
                    </div>

                    {user.registrationDate && (
                        <div className="mb-4">
                            <label className="font-semibold">Registration date:</label>
                            <p className="text-gray-700">{formDate(user.registrationDate)[0]} {formDate(user.registrationDate)[1]} {formDate(user.registrationDate)[2]}</p>
                        </div>
                    )}

                    {user.rating && (
                        <div className="mb-4">
                            <label className="font-semibold">The number of completed tasks in various fields:</label>

                            {user.fieldRatingList.length === 0 ? (
                                <p>At the moment, you has not yet completed any tasks.</p>
                            ) : (<table className="w-full mt-2 border">
                                <thead>
                                <tr>
                                    <th className="px-4 py-2 border-b">Field of Activity</th>
                                    <th className="px-4 py-2 border-b">Amount</th>
                                </tr>
                                </thead>
                                <tbody>
                                {user.fieldRatingList.map((task, index) => (
                                    <tr key={index}>
                                        <td className="px-4 py-2 border-b">{task.name}</td>
                                        <td className="px-4 py-2 border-b">{task.taskCompleted}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </table>)}

                        </div>
                    )}

                    <div className="flex justify-between mt-4">
                        <div className="ml-3">
                            <button
                                onClick={handleUpdate}
                                className="px-4 py-2 text-white bg-green-500 rounded hover:bg-green-600"
                            >
                                Update
                            </button>
                        </div>

                        <div className="mr-3">
                            <button
                                onClick={toggleDeactivate}
                                className="px-4 py-2 text-white bg-red-500 rounded hover:bg-red-600"
                            >
                                Deactivate
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            {
                isDeactivated && (
                    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
                        <div className="relative w-full max-w-lg p-6 bg-white rounded-lg shadow-lg">
                            <button
                                className="absolute top-2 right-2 text-gray-500 hover:text-gray-700"
                                onClick={toggleDeactivate}
                            >
                                ✖
                            </button>

                            <h2 className="mb-4 text-2xl font-semibold text-center"> Are you sure you want to deactivate
                                profile?</h2>
                            <p>
                                By deactivating your account you will no longer be able to log into this account.
                            </p>
                            <div className="flex justify-between mt-4">
                                <div className="ml-3">
                                    <button
                                        onClick={() => handleDeactivateProfile()}
                                        className="px-4 py-2 text-white bg-red-500 rounded hover:bg-red-600"
                                    >
                                        Yes, I'm sure
                                    </button>
                                </div>

                                <div className="mr-3">
                                    <button
                                        onClick={toggleDeactivate}
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
    )
}