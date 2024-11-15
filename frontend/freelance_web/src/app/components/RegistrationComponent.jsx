import {useState} from "react";
import * as Yup from "yup";
import {ErrorMessage, Field, Form, Formik} from "formik";
import {Link, useNavigate} from "react-router-dom";
import {registration} from "../api/GuestApi";

export default function RegistrationComponent() {
    const [showErrorMessage, setShowErrorMessage] = useState(false)
    const [showPasswordMessage, setShowPasswordMessage] = useState(false)
    const navigator = useNavigate()

    function handleSubmit(value) {
        if (value.password !== value.confirmPassword) {
            setShowPasswordMessage(true)
        }
        else {
            Promise.all([registration(value)])
                .then(result => {
                    navigator("/")
                })
                .catch(error => {
                    setShowErrorMessage(true)
                })
        }
    }

    const validationSchema = Yup.object().shape({
        email: Yup.string()
            .email("Should be email")
            .required("Value can't be empty"),

        password: Yup.string()
            .min(8, "Password must contain at least 8 characters")
            .required("Value can't be empty"),

        name: Yup.string()
            .required("Value can't be empty"),

        secondName: Yup.string()
            .required("Value can't be empty"),

        surname: Yup.string()
            .required("Value can't be empty"),

        birthday: Yup.date()
            .max(new Date(), "Birthday must be in the past")
            .required("Value can't be empty"),

        gender: Yup.string()
            .required("Value can't be empty"),

        phoneNum: Yup.string()
            .matches(
                /^(?:\+?\d{1,3}(?: )?)?(?:(\(\d{3}\))|\d{3})[- .]?\d{3}[- .]?\d{4}$|^(?:\+?\d{1,3}(?: )?)?(?:\d{3}[ ]?){2}\d{3}$|^(?:\+?\d{1,3}(?: )?)?(?:\d{3}[ ]?)(?:\d{2}[ ]?){2}\d{2}$/,
                "Must follow phone pattern"
            )
            .required("Value can't be empty"),

        aboutMe: Yup.string()
            .required("Value can't be empty"),

        role: Yup.string()
            .required("Value can't be empty")
    })

    return (
        <div className="flex flex-col justify-center items-center mt-10 pb-8">
            <div className="w-1/3 p-10 bg-gray-100 rounded-lg">
                    <h2 className="mb-4 text-2xl font-semibold text-center"> Registration form </h2>

                <Formik
                    initialValues={{
                        email: "",
                        password: "",
                        confirmPassword: "",
                        name: "",
                        secondName: "",
                        surname: "",
                        birthday: "",
                        gender: "",
                        phoneNum: "",
                        aboutMe: "",
                        role: ""
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
                                <label htmlFor="role" className="block mb-1 font-semibold">Role</label>
                                <Field as="select" name="role" className="w-full p-2 border rounded">
                                    <option value="" disabled={true}>Select a role</option>
                                    <option value="CUSTOMER">Customer</option>
                                    <option value="CONTRACTOR">Contractor</option>
                                </Field>
                                <ErrorMessage name="role" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="email" className="block mb-1 font-semibold">Email</label>
                                <Field type="text" name="email" className="w-full p-2 border rounded"/>
                                <ErrorMessage name="email" component="div" className="text-red-500"/>
                            </div>
                            {showErrorMessage &&
                                <div className="text-red-500">This email is already in use.</div>}

                            <div className="mb-4">
                                <label htmlFor="password" className="block mb-1 font-semibold">Password</label>
                                <Field type="password" name="password" className="w-full p-2 border rounded"/>
                                <ErrorMessage name="password" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="confirmPassword" className="block mb-1 font-semibold">Confirm
                                    password</label>
                                <Field type="password" name="confirmPassword" className="w-full p-2 border rounded"/>
                                <ErrorMessage name="confirmPassword" component="div" className="text-red-500"/>
                            </div>
                            {showPasswordMessage &&
                                <div className="text-red-500">The passwords entered do not match.</div>}

                            <div className="mb-4">
                                <label htmlFor="name" className="block mb-1 font-semibold">Firstname</label>
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
                                <label htmlFor="birthday" className="block mb-1 font-semibold">Birthday date</label>
                                <Field
                                    type="date"
                                    name="birthday"
                                    className="w-full p-2 border rounded"
                                />
                                <ErrorMessage name="birthday" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="gender" className="block mb-1 font-semibold">Gender</label>
                                <Field as="select" name="gender" className="w-full p-2 border rounded">
                                    <option value="" disabled={true}>Select your gender</option>
                                    <option value="Male">Male</option>
                                    <option value="Female">Female</option>
                                    <option value="Other">Other</option>
                                </Field>
                                <ErrorMessage name="gender" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="phoneNum" className="block mb-1 font-semibold">Phone number</label>
                                <Field type="text" name="phoneNum" className="w-full p-2 border rounded"/>
                                <ErrorMessage name="phoneNum" component="div" className="text-red-500"/>
                            </div>

                            <div className="mb-4">
                                <label htmlFor="aboutMe" className="block mb-1 font-semibold">About</label>
                                <Field as="textarea" name="aboutMe" className="w-full p-2 border rounded" rows="3"/>
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
                            <div className="flex justify-center mt-4">
                                <Link to={"/"}>Return to login page</Link>
                            </div>
                        </Form>
                    )}
                </Formik>
            </div>
        </div>
    )
}