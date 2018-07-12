class ErrorContainer {

    constructor() {
        this.errors = [];
        this._nextId = 0;
    }

    add(error) {
        const element = {
            timestamp: Date.now(),
            id: this._nextId,
            error
        }
        this.errors.push(element);
        // remove after 5 seconds
        setTimeout(() => this._remove(element), 5000);
        this._nextId += 1;
    }

    _remove(element) {
        const i = this.errors.indexOf(element);
        if (i >= 0) {
            this.errors.splice(i, 1);
        }
    }
}

export default new ErrorContainer();