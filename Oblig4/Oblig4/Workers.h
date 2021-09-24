#pragma once
#include <vector>
#include <thread>
#include <mutex>
#include <iostream>
#include <condition_variable>
#include <list>
#include <functional>


using namespace std;
class Workers{

public: 
	Workers(int nrThreads);

	void post(function<void()> task);
	void threads_close(); 
	void timeout(function<void()> task, int ms); 
	void start_thread(); 
	void stop(); 

private:
	void work(function<void()> &task, bool &done);
	
	int nrWorkers; 
	list<thread> worker_threads; 
	condition_variable cv; 
	mutex m; 
	mutex wait_mutex; 
	list<function<void()>> tasks; 
	bool needToWait = true; 
	bool stopped = false;
};

