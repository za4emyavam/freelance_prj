import {ErrorMessage, Field, FieldArray, Form, Formik} from "formik";
import * as Yup from "yup";
import {useEffect, useState} from "react";
import {retrieveCustomerTaskByTaskId, retrieveFields, saveTask, updateTask} from "../../api/CustomerApi";
import {useNavigate, useParams} from "react-router-dom";

export default function CreateTask() {
    const {taskId} = useParams()
    const [task, setTask] = useState("")
    const [fields, setFields] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const navigator = useNavigate()

    useEffect(() => {
        refreshPage()
    }, [])

    function refreshPage() {
        setIsLoading(true)
        if (taskId == null) {
            Promise.all([
                retrieveFields(),
            ]).then((responses) => {
                setFields(responses[0].data)
            }).catch((error) => {
                if (error.response.data === "")
                    return navigator(`/error`)
            })
                .finally(() => setIsLoading(false))
        } else {
            Promise.all([
                retrieveFields(),
                retrieveCustomerTaskByTaskId(taskId)
            ]).then((responses) => {
                setFields(responses[0].data)
                setTask(responses[1].data)
            }).catch((error) => {
                if (error.response.data === "")
                    return navigator(`/error`)
            })
                .finally(() => setIsLoading(false))
        }

    }

    const validationSchema = Yup.object().shape({
        idActivityField: Yup.string().required("This field is required"),
        name: Yup.string().required("This field is required"),
        endDate: Yup.date().required("This field is required").min(new Date(), "The date must be in the future"),
        cost: Yup.number().typeError("Must be a number").required("This field is required")
            .min(10, "Number must be at least 10 dollars")
            .max(5000, "Number must be 5000 dollars or less"),
        description: Yup.string().required("This field is required"),
        links: Yup.array().of(Yup.string().required("Link cannot be empty")).min(1, "At least one link is required"),
    })

    const handleSubmit = (values) => {
        if (taskId == null) {
            Promise.all([saveTask(values)])
                .then(() => {
                    navigator(`/customer/tasks`)
                })
        } else {
            Promise.all([updateTask(taskId, values)])
                .then(() => {
                    navigator(`/customer/tasks`)
                })
        }

    }

    if (isLoading) {
        return (
            <div>

            </div>
        )
    }

    return (
        <div className="flex flex-col justify-center items-center mt-10">
            <div className="w-1/3 p-10 bg-gray-100 rounded-lg">
                {taskId == null ? (<h2 className="mb-4 text-2xl font-semibold text-center"> Create new task </h2>) : (
                    <h2 className="mb-4 text-2xl font-semibold text-center"> Update task </h2>)}

                <Formik
                    initialValues={{
                        idActivityField: taskId == null ? "" : task.fieldOfActivity.idField,
                        name: taskId == null ? "" : task.name,
                        endDate: taskId == null ? "" : task.endDate,
                        cost: taskId == null ? "" : task.cost,
                        description: taskId == null ? "" : task.description,
                        links: taskId == null ? [""] : task.links.map((link) => (link.link)),
                    }}
                    validationSchema={validationSchema}
                    onSubmit={(values, {resetForm}) => {
                        handleSubmit(values)
                    }}
                >
                    {({values, isSubmitting}) => (
                        <Form>
                            {/* Select for field */}
                            <div className="mb-4">
                                <label htmlFor="idActivityField" className="block mb-1 font-semibold">Field</label>
                                <Field as="select" name="idActivityField" className="w-full p-2 border rounded">
                                    <option value="" disabled={true}>Select a field</option>
                                    {fields.map((field) => (
                                        <option key={field.idField} value={field.idField}>{field.field}</option>
                                    ))}
                                </Field>
                                <ErrorMessage name="idActivityField" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="name" className="block mb-1 font-semibold">Title</label>
                                <Field type="text" name="name" className="w-full p-2 border rounded"/>
                                <ErrorMessage name="name" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="endDate" className="block mb-1 font-semibold">End date</label>
                                <Field
                                    type="datetime-local"
                                    name="endDate"
                                    className="w-full p-2 border rounded"
                                />
                                <ErrorMessage name="endDate" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="cost" className="block mb-1 font-semibold">Cost</label>
                                <Field type="number" name="cost" className="w-full p-2 border rounded"/>
                                <ErrorMessage name="cost" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="description" className="block mb-1 font-semibold">Description</label>
                                <Field as="textarea" name="description" className="w-full p-2 border rounded" rows="3"/>
                                <ErrorMessage name="description" component="div" className="text-red-500"/>
                            </div>

                            {/* Links Array */}
                            <div className="mb-4">
                                <label className="block mb-1 font-semibold">Links</label>
                                <FieldArray name="links">
                                    {({push, remove}) => (
                                        <div>
                                            {values.links.map((tag, index) => (
                                                <div key={index} className="flex items-center mb-2">
                                                    <Field
                                                        type="text"
                                                        name={`links[${index}]`}
                                                        placeholder={`Link ${index + 1}`}
                                                        className="w-full p-2 border rounded"
                                                    />
                                                    <button
                                                        type="button"
                                                        onClick={() => remove(index)}
                                                        className="ml-2 text-red-500"
                                                    >
                                                        ✖
                                                    </button>
                                                </div>
                                            ))}
                                            <button
                                                type="button"
                                                onClick={() => push("")}
                                                className="px-3 py-1 mt-2 text-white bg-blue-500 rounded hover:bg-blue-600"
                                            >
                                                Add Link
                                            </button>
                                        </div>
                                    )}
                                </FieldArray>
                                <ErrorMessage name="links" component="div" className="text-red-500"/>
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