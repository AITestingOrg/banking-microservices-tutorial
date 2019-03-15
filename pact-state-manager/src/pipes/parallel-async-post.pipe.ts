import { ArgumentMetadata, Injectable, PipeTransform } from '@nestjs/common';
import {async} from "async";
import {EntityMap} from "../models/entity-map";

@Injectable()
export class ParallelAsyncPostPipe implements PipeTransform {
  transform(value: Array<EntityMap>, metadata: ArgumentMetadata) {
    const jobs = this.buildEntityJobs(value);
    async.parallel(
        jobs,
        (err, results) => {
          throw new Error(`Failed to POST all entities: ${err}`);
        });
    return value;
  }

  private buildEntityJobs(entityMaps: Array<EntityMap>): Array<(callback) => void> {
    return entityMaps.map((entityMap) => (callback) => {
      async.waterfall([], (err, result) => {
        if (err) {
          callback(err);
        }
        callback();
      })
    });
  }

  private buildPostJobs() {

  }
}
