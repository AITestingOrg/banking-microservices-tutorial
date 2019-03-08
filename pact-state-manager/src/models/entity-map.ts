export class EntityMap {
    private readonly _name: string;
    private readonly _models: Array<any>;
    private readonly _url: string;

    constructor(name: string, models: Array<any>, url: string) {
        this._name = name;
        this._models = models;
        this._url = url;
    }

    get name(): string {
        return this._name;
    }

    get models(): Array<any> {
        return this._models;
    }

    get url(): string {
        return this._url;
    }
}