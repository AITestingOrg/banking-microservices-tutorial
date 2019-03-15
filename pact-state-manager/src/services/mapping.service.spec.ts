import { Test, TestingModule } from '@nestjs/testing';
import { MappingService } from './mapping.service';

describe('MappingService', () => {
  let service: MappingService;

  beforeEach(async () => {
    const module: TestingModule = await Test.createTestingModule({
      providers: [MappingService],
    }).compile();

    service = module.get<MappingService>(MappingService);
  });

  it('should be defined', () => {
    expect(service).toBeDefined();
  });
});
