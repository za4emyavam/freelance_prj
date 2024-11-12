function Pagination({ totalCount, offset, onPageChange }) {
    const pageSize = 5
    const totalPages = Math.ceil(totalCount / pageSize);
    const currentPage = Math.floor(offset / pageSize) + 1;

    const handlePrevious = () => {
        if (offset > 0) {
            onPageChange(offset - pageSize);
        }
    };

    const handleNext = () => {
        if (offset < totalCount - pageSize) {
            onPageChange(offset + pageSize);
        }
    };

    const handlePageClick = (page) => {
        onPageChange((page - 1) * pageSize);
    };

    return (
        <ul className="inline-flex -space-x-px rtl:space-x-reverse text-sm h-8">
            <li>
                <button
                    onClick={handlePrevious}
                    disabled={offset === 0}
                    className="flex items-center justify-center px-3 h-8 leading-tight text-gray-400 border-gray-300 rounded-s-lg hover:bg-gray-700 hover:text-gray-100 bg-gray-800"
                >
                    Previous
                </button>
            </li>
            {Array.from({ length: totalPages }, (_, index) => (
                <li key={index + 1}>
                    <button
                        onClick={() => handlePageClick(index + 1)}
                        className={`flex items-center justify-center px-3 h-8 leading-tight ${
                            currentPage === index + 1
                                ? "text-gray-200 bg-gray-600 border border-gray-600"
                                : "text-gray-500 bg-gray-800 border border-gray-600 hover:bg-gray-700 hover:text-gray-200"
                        }`}
                    >
                        {index + 1}
                    </button>
                </li>
            ))}
            <li>
                <button
                    onClick={handleNext}
                    disabled={offset >= totalCount - pageSize}
                    className="flex items-center justify-center px-3 h-8 leading-tight text-gray-400 border-gray-300 rounded-e-lg hover:bg-gray-700 hover:text-gray-100 bg-gray-800"
                >
                    Next
                </button>
            </li>
        </ul>
    );
}

export default Pagination;