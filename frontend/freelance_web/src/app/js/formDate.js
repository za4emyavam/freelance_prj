export function formDate(dateStr) {
    const date = new Date(dateStr)
    const options = { month: 'short' };

    const ukrainianMonths = new Intl.DateTimeFormat('en-US', options);
    return [date.getDate(), ukrainianMonths.format(date), date.getFullYear(),
        (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':' + (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes())]
}