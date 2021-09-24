#include "Workers.h"

Workers::Workers(int nrWorkers) {
	this->nrWorkers = nrWorkers; 
}


void Workers::stop() {
	stopped = true; 
}

void Workers::start_thread() {
	for (int i = 0; i < nrWorkers; i++) {
		worker_threads.emplace_back([this] {
			bool done = false;
			while (!done) {
				function<void()> task;
				if (tasks.empty()) {
					unique_lock<mutex> ul(wait_mutex);
					while (needToWait) {
						cv.wait(ul); 
					}
				}
				work(task, done); 

				if (task) {
					task(); 
					cout << "task being done by: " << this_thread::get_id() << "\n";

					cv.notify_one(); 
				}
			}

		});
	}

}


void Workers::work(function<void()> &task, bool &done) {
	{ // operating on tasks-object
		unique_lock<mutex> ul(m);
		if (!tasks.empty()) {
			task = *tasks.begin();
			tasks.pop_front();
		}
		else {
			if (stopped) {
				done = true;
			}
			else {
				unique_lock<mutex> ul(wait_mutex);
				needToWait = true; 
			}
		}
	}
}




void Workers::post(function<void()> task) {
	{
		unique_lock<mutex> lock(m);
		tasks.emplace_back(task);
	}

	{
		unique_lock<mutex> lock(wait_mutex);
		needToWait = false; 
	}
	cv.notify_all(); 
}


void Workers::threads_close() {
	for (auto& thread : worker_threads) {
		thread.join(); 
	}
}

void Workers::timeout(function<void()> task, int ms) {
	unique_lock<mutex> ul(m);
	tasks.emplace_back([task, ms] {
		this_thread::sleep_for(chrono::milliseconds(ms));
		task(); 
		});
	cv.notify_one();

}

