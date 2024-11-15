import {ErrorMessage, Field, FieldArray, Form, Formik} from "formik";
import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import {createField, retrieveFieldById, updateField} from "../../api/AdminApi";
import * as Yup from "yup";

export default function FieldComponent() {
    const {fieldId} = useParams()
    const [field, setField] = useState("")
    const [showErrorMessage, setShowErrorMessage] = useState(false)
    const [isLoading, setIsLoading] = useState(true)
    const navigator = useNavigate()

    useEffect(() => {
        retrieveData()
    }, [])

    function retrieveData() {
        setIsLoading(true)
        if (fieldId != null) {
            Promise.all([retrieveFieldById(fieldId)])
                .then((responses) => {
                    setField(responses[0].data)
                })
                .finally(() => setIsLoading(false))
        } else {
            setIsLoading(false)
        }
    }

    const handleSubmit = (values) => {
        const val = {
            idField: fieldId,
            field: values.name,
            isActive: values.isActive,
        }
        if (fieldId != null) {
            Promise.all([updateField(val)])
                .then((responses) => {
                    navigator("/admin/fields")
                })
                .catch(() => setShowErrorMessage(true))
        } else {
            Promise.all([createField(val)])
                .then((responses) => {
                    navigator("/admin/fields")
                })
                .catch(() => setShowErrorMessage(true))
        }
    }

    const validationSchema = Yup.object().shape({
        name: Yup.string().required("This field is required"),
        isActive: Yup.boolean().required("This field is required")
    });

    if (isLoading)
        return (
            <div>

            </div>
        )

    return (
        <div className="flex flex-col justify-center items-center mt-10">
        <div className="w-1/3 p-10 bg-gray-100 rounded-lg">
            {fieldId == null ? (<h2 className="mb-4 text-2xl font-semibold text-center"> Create new field of activity</h2>) : (
                <h2 className="mb-4 text-2xl font-semibold text-center"> Update field of activity</h2>)}

            <Formik
                initialValues={{
                    name: fieldId == null ? "" : field.field,
                    isActive: fieldId == null ? "false" : field.isActive,
                }}
                validationSchema={validationSchema}
                onSubmit={(values) => {
                    handleSubmit(values)
                }}
            >
                {({values, isSubmitting}) => (
                    <Form>

                        <div className="mb-4">
                            <label htmlFor="name" className="block mb-1 font-semibold">Field of activity name</label>
                            <Field type="text" name="name" className="w-full p-2 border rounded"/>
                            <ErrorMessage name="name" component="div" className="text-red-500"/>
                            {showErrorMessage &&
                                <div className="text-red-500">This name is already in use</div>}
                        </div>


                        <div className="mb-4">
                            <label htmlFor="isActive" className="block mb-1 font-semibold">Status</label>
                            <Field as="select" name="isActive" className="w-full p-2 border rounded">
                                <option value="" disabled={true}>Select is active</option>
                                <option value="true">Active</option>
                                <option value="false">Not active</option>
                            </Field>
                            <ErrorMessage name="isActive" component="div" className="text-red-500"/>
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