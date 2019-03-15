import { Injectable } from '@nestjs/common';
import {defaultModelMap} from "../config/mappings";

@Injectable()
export class MappingService {
    private mappings: any;
    constructor() {}

    updateMapping(mapping: any) {

    }

    createMapping(mapping: any) {

    }

    getMappings(): any {

    }

    private getStoredMappings(): any {
        if (!this.mappings) {
            this.mappings = defaultModelMap;
        }
        return this.mappings;
    }
}
