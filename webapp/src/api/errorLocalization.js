class ErrorLocalization {

    getForAxios(error) {
        if (error.response) {
            // The request was made and the server responded with an error status code
            switch (error.response.status) {
                case 404:
                    return `The requested resource does not exist. Try going back to the last page.`;
                case 500:
                    return `An internal error occured. We're sorry, please try again later.`;
                default:
                    return `The server returned an error: ${error.response.status} ${error.response.statusText}`;
            }
        } else if (error.request) {
            // The request was made but no response was received
            return `The server did not respond. Try checking your internet connection.`;
        }
        return null;
    }

    getForContract(error) {
        // TODO filter for specific errors
        return "An error occured with your Ethereum provider. Please check your transaction log or try again.";
    }
}

export default new ErrorLocalization();