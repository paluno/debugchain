class ErrorLocalization {

    getForAxios(error, serviceName) {
        if (error.response) {
            // The request was made and the server responded with an error status code
            switch (error.response.status) {
                case 404:
                    return `The requested resource does not exist${serviceName ? " in " + serviceName : ""}. Try going back to the last page.`;
                case 500:
                    return `An internal error occured${serviceName ? " in " + serviceName : ""}. We're sorry, please try again later.`;
                default:
                    return `The${serviceName ? " in " + serviceName : ""} server returned an error: ${error.response.status} ${error.response.statusText}`;
            }
        } else if (error.request) {
            // The request was made but no response was received
            return `The${serviceName ? " in " + serviceName : ""} server did not respond. Try checking your internet connection.`;
        }
        return null;
    }

    getForContract(error) {
        switch (error.message) {
            case "Could not retrieve meta-mask provider":
                return "Unable to retrieve Ethereum provider. Make sure that you have an ethereum client (e.g. MetaMask) installed and unlocked.";
            case "No web3 account available":
                return "Could not access your account. Did you unlock your wallet?";
        }
        return "An error occured with your Ethereum provider. Please check your transaction log or try again.";
    }
}

export default new ErrorLocalization();