import { ArgumentMetadata, Injectable, PipeTransform } from '@nestjs/common';
import {EntityMap} from "../models/entity-map";
import {modelMap} from "../config/mappings";

@Injectable()
export class ExtractModelsPipe implements PipeTransform {
  transform(value: any, metadata: ArgumentMetadata): Array<EntityMap> {
    const mappedEntities = new Array<EntityMap>();
    Object.keys(value).forEach(key => {
      if (!(key in modelMap)) {
        throw new Error(`No models mapped for type ${key}.`);
      }
      mappedEntities.push(new EntityMap(key, value[key], modelMap[key]));
    });
    return mappedEntities;
  }
}
