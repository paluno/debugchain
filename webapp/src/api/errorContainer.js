class ErrorContainer {

    constructor() {
        this.errors = [];
        this._nextId = 0;
    }

    add(error) {
        error = this._ensureError(error);

        const element = {
            timestamp: Date.now(),
            id: this._nextId,
            raw: error
        }
        this.errors.push(element);
        console.error("User message: " + error.userMessage, error);
        // remove after 5 seconds
        setTimeout(() => this._remove(element), 5000);
        this._nextId += 1;
    }

    _ensureError(error) {
        if (error instanceof Error) {
            return error;
        } else {
            if (typeof error === 'string') {
                return new Error(error);
            }
        }
        return new Error("An unknown error occured.");
    }

    _remove(element) {
        const i = this.errors.indexOf(element);
        if (i >= 0) {
            this.errors.splice(i, 1);
        }
    }
}

export default new ErrorContainer();