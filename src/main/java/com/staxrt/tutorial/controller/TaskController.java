/*
 *
 *  Copyright (c) 2018-2020 Givantha Kalansuriya, This source is a part of
 *   Staxrt - sample application source code.
 *   http://staxrt.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.staxrt.tutorial.controller;

import com.staxrt.tutorial.exception.ResourceNotFoundException;
import com.staxrt.tutorial.model.Task;
import com.staxrt.tutorial.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TaskController {

  @Autowired
  private TaskRepository taskRepository;

  @GetMapping("/tasks")
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  @GetMapping("/tasks/{id}")
  public ResponseEntity<Task> getTasksById(@PathVariable(value = "id") Long taskId) throws ResourceNotFoundException {

    Task task = taskRepository
            .findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found on :: " + taskId));

    return ResponseEntity.ok().body(task);
  }

  @PostMapping("/tasks")
  public Task createTask(@Valid @RequestBody Task task) {
    return taskRepository.save(task);
  }

  @PutMapping("/tasks/{id}")
  public ResponseEntity<Task> updateTask(@PathVariable(value = "id") Long taskId, @RequestBody Task taskDetails)
      throws ResourceNotFoundException {

    Task task = taskRepository
            .findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found on :: " + taskId));

    task.setTitle(taskDetails.getTitle());
    task.setDescription(taskDetails.getDescription());
    task.setDate(taskDetails.getDate());

    final Task updatedUser = taskRepository.save(task);
    return ResponseEntity.ok(updatedUser);
  }

  @DeleteMapping("/task/{id}")
  public Map<String, Boolean> deleteTask(@PathVariable(value = "id") Long taskId) throws Exception {
    Task task =
            taskRepository
            .findById(taskId)
            .orElseThrow(() -> new ResourceNotFoundException("Task not found on :: " + taskId));

    taskRepository.delete(task);
    Map<String, Boolean> response = new HashMap<>();
    response.put("deleted", Boolean.TRUE);
    return response;
  }
}
