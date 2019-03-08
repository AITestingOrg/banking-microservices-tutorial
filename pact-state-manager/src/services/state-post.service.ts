import {HttpService, Injectable} from '@nestjs/common';
import { EntityMap } from '../models/entity-map';
import { async } from 'async';

@Injectable()
export class StatePostService {
  constructor(private http: HttpService) {}

  async pushEntities(entityMaps: Array<EntityMap>) {
    const jobs = this.buildEntityJobs(entityMaps);
    await Promise.all(jobs);
  }

  private buildEntityJobs(entityMaps: Array<EntityMap>): Promise<void>[] {
    return entityMaps.map(async (entityMap: EntityMap) => {
      for (let model in entityMap.models) {
        await this.postModel(entityMap.url, model);
      }
    });
  }

  private async postModel(url: string, model: any) {
    await this.http.post(url, model);
  }
}
