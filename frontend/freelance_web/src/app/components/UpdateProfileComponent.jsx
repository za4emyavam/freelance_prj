import {ErrorMessage, Field, FieldArray, Form, Formik} from "formik";
import {useEffect, useState} from "react";
import * as Yup from "yup";
import {retrieveUserInfo, updateProfile} from "../api/CustomerApi";
import {useNavigate} from "react-router-dom";
import {updateContractorProfile} from "../api/ContractorApi";

export default function UpdateProfileComponent() {
    const [user, setUser] = useState("")
    const [isLoading, setIsLoading] = useState(true)
    const navigator = useNavigate()

    useEffect(() => {
        refreshPage()
    }, [])

    function refreshPage() {
        setIsLoading(true)
        Promise.all([retrieveUserInfo()])
            .then((responses) => {
                setUser(responses[0].data);
            })
            .finally(() => setIsLoading(false))
    }

    const handleSubmit = (values) => {
        if (localStorage.getItem('role') === "CUSTOMER") {
            Promise.all([updateProfile(values)])
                .then((response) => {
                    navigator(`/customer/profile`)
                })
        }
        if (localStorage.getItem('role') === "CONTRACTOR") {
            Promise.all([updateContractorProfile(values)])
                .then((response) => {
                    navigator(`/contractor/profile`)
                })
        }
    }

    const validationSchema = Yup.object().shape({
        name: Yup.string().required("This field is required"),
        secondName: Yup.string().required("This field is required"),
        surname: Yup.string().required("This field is required"),
        birthday: Yup.date().required("This field is required").max(new Date(), "The date must be in the past"),
        gender: Yup.string().required("This field is required"),
        phoneNum: Yup.string().required("This field is required"),
        aboutMe: Yup.string().required("This field is required")
    });

    if (isLoading) {
        return (
            <div>

            </div>
        )
    }

    return (
        <div>
            <div className="flex flex-col justify-center items-center mt-10">
                <div className="w-1/3 p-10 bg-gray-100 rounded-lg">
                    <h2 className="mb-4 text-2xl font-semibold text-center"> Update profile</h2>

                    <Formik
                        initialValues={{
                            name: user.name,
                            secondName: user.secondName,
                            surname: user.surname,
                            birthday: user.birthday,
                            gender: user.gender,
                            phoneNum: user.phoneNum,
                            aboutMe: user.aboutMe,
                        }}
                        validationSchema={validationSchema}
                        onSubmit={(values, {resetForm}) => {
                            handleSubmit(values)
                        }}
                    >
                        {({values, isSubmitting}) => (
                            <Form>
                                <div className="mb-4">
                                    <label htmlFor="name" className="block mb-1 font-semibold">First name</label>
                                    <Field type="text" name="name" className="w-full p-2 border rounded"/>
                                    <ErrorMessage name="name" component="div" className="text-red-500"/>
                                </div>

                                <div className="mb-4">
                                    <label htmlFor="secondName" className="block mb-1 font-semibold">Second name</label>
                                    <Field type="text" name="secondName" className="w-full p-2 border rounded"/>
                                    <ErrorMessage name="secondName" component="div" className="text-red-500"/>
                                </div>

                                <div className="mb-4">
                                    <label htmlFor="surname" className="block mb-1 font-semibold">Surname</label>
                                    <Field type="text" name="surname" className="w-full p-2 border rounded"/>
                                    <ErrorMessage name="surname" component="div" className="text-red-500"/>
                                </div>

                                <div className="mb-4">
                                    <label htmlFor="birthday" className="block mb-1 font-semibold">Birthday</label>
                                    <Field
                                        type="date"
                                        name="birthday"
                                        className="w-full p-2 border rounded"
                                    />
                                    <ErrorMessage name="birthday" component="div" className="text-red-500"/>
                                </div>

                                <div className="mb-4">
                                    <label htmlFor="gender" className="block mb-1 font-semibold">Gender</label>
                                    <Field type="text" name="gender" className="w-full p-2 border rounded"/>
                                    <ErrorMessage name="gender" component="div" className="text-red-500"/>
                                </div>

                                <div className="mb-4">
                                    <label htmlFor="phoneNum" className="block mb-1 font-semibold">Phone number</label>
                                    <Field type="text" name="phoneNum" className="w-full p-2 border rounded"/>
                                    <ErrorMessage name="phoneNum" component="div" className="text-red-500"/>
                                </div>

                                <div className="mb-4">
                                    <label htmlFor="aboutMe"
                                           className="block mb-1 font-semibold">About me</label>
                                    <Field as="textarea" name="aboutMe" className="w-full p-2 border rounded"
                                           rows="3"/>
                                    <ErrorMessage name="aboutMe" component="div" className="text-red-500"/>
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
        </div>
    )
}