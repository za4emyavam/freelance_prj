import getDefRoutes from "../js/getDefRoutes";

export default function NoContentComponent() {
    return (
        <div className="flex items-center justify-center min-h-[86vh] bg-gray-100">
            <div className="text-center p-6 bg-white rounded-lg shadow-lg">
                <h1 className="text-2xl font-semibold text-gray-800">No content available</h1>
                <p className="mt-4 text-gray-600">Sorry, there is currently no content to display.</p>
                <a
                    href={getDefRoutes()}
                    className="mt-6 inline-block px-4 py-2 text-white bg-blue-600 rounded hover:bg-blue-700 transition-colors"
                >
                    Return to the main page
                </a>
            </div>
        </div>
    )
}