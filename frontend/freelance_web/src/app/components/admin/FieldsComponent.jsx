import {useEffect, useState} from "react";
import {retrieveAllFields} from "../../api/AdminApi";
import {Link, useNavigate} from "react-router-dom";

export default function FieldsComponent() {
    const [fields, setFields] = useState([])
    const [isLoading, setIsLoading] = useState(true)
    const navigator = useNavigate();

    useEffect(() => {
        retrieveDate()
    }, [])

    function retrieveDate() {
        setIsLoading(true)
        Promise.all([retrieveAllFields()])
            .then((responses) => {
                setFields(responses[0].data)
            })
            .finally(() => setIsLoading(false))
    }

    if (isLoading)
        return (
            <div>

            </div>
        )

    return (
        <div className="mt-3 ml-3 p-4">
            <div className="flex flex-row mb-[10px]">
                <h1 className="text-3xl font-bold mb-6">Fields of activity list</h1>
                <Link to="/admin/fields/create"
                      className="text-xl font-semibold flex justify-between items-center mt-[-10px] ml-12 px-3 py-1 text-gray-400 bg-gray-700 rounded-lg hover:bg-gray-400 hover:text-gray-800">
                    Create new field of activity
                </Link>
            </div>
            <div className="grid grid-cols-1 gap-6 md:grid-cols-2 lg:grid-cols-5">
                {fields.map(field => (
                    <Link to={`/admin/fields/${field.idField}`} key={field.idField}
                          className="bg-white shadow-md rounded-lg p-6 no-underline text-black">
                        <h2 className="text-xl font-semibold mb-1">{field.field}</h2>
                        <p className="text-gray-700">ID: {field.idField}</p>
                        <p className="text-gray-700">{field.isActive ? "Active" : "Not active"}</p>
                    </Link>
                ))}
            </div>
        </div>
    )
}