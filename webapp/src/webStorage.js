// Helper functions
function set(storage, key, obj) {
    if (obj === undefined) {
        return;
    }
    if (obj == null) {
        storage.removeItem(key);
    }
    else {
        storage.setItem(key, JSON.stringify(obj));
    }
}
function get(storage, key) {
    const obj = storage.getItem(key);
    if (obj == null) {
        return null;
    }
    return JSON.parse(obj);
}

function setSession(key, obj) {
    set(sessionStorage, key, obj)
}
function getSession(key) {
    return get(sessionStorage, key)
}

function setLocal(key, obj) {
    set(localStorage, key, obj)
}
function getLocal(key) {
    return get(localStorage, key)
}

/**
 * A localStorage / sessionStorage abstraction.
 * 
 * Provides convenience methods to access predefined key/value pairs.
 */
export default {
    setLoginRedirect: function (obj) {
        setSession("loginRedirect", obj);
    },
    getLoginRedirect: function () {
        return getSession("loginRedirect");
    }
}