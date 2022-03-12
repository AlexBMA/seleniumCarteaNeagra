import { TestBed } from '@angular/core/testing';

import { FileOperationServiceService } from './file-operation-service.service';

describe('FileOperationServiceService', () => {
  let service: FileOperationServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FileOperationServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
